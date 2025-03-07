package eightplusone.bit.fit.domain.auth.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import eightplusone.bit.fit.domain.auth.dto.CustomOAuth2User;
import eightplusone.bit.fit.domain.auth.dto.GoogleResponse;
import eightplusone.bit.fit.domain.auth.dto.KakaoResponse;
import eightplusone.bit.fit.domain.auth.dto.NaverResponse;
import eightplusone.bit.fit.domain.auth.dto.OAuth2Response;
import eightplusone.bit.fit.domain.auth.dto.OAuth2UserDto;
import eightplusone.bit.fit.domain.auth.enums.Role;
import eightplusone.bit.fit.domain.user.entity.User;
import eightplusone.bit.fit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;
	private final RedisTokenService redisTokenService;
	private static final long ACCESS_TOKEN_EXPIRATION = 3600 * 1000;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = super.loadUser(userRequest);

		/* 회원 탈퇴 토큰 추출 */
		String oauth2AccessToken = userRequest.getAccessToken().getTokenValue();

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;

		if (registrationId.equals("naver")) {
			oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
		} else if (registrationId.equals("kakao")) {
			oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
		} else if (registrationId.equals("google")) {
			oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
		} else {
			return null;
		}
		String provider = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();

		if (!userRepository.existsByProvider(provider)) {
			if (userRepository.existsByEmail(oAuth2Response.getEmail())) {
				throw new OAuth2AuthenticationException("중복 소셜 회원 가입");
			}
			userRepository.save(User.of(
				oAuth2Response.getEmail(),
				oAuth2Response.getName(),
				provider,
				Role.USER)
			);

			redisTokenService.saveOauth2AccessToken(provider, oauth2AccessToken, ACCESS_TOKEN_EXPIRATION);

			return new CustomOAuth2User(OAuth2UserDto.of(
				provider,
				oAuth2Response.getEmail(),
				oAuth2Response.getName(),
				Role.USER)
			);
		}

		if (redisTokenService.getOauth2AccessToken(provider) != null) {
			redisTokenService.deleteOauth2AccessToken(provider);
		}
		redisTokenService.saveOauth2AccessToken(provider, oauth2AccessToken, ACCESS_TOKEN_EXPIRATION);

		return new CustomOAuth2User(OAuth2UserDto.of(
			provider,
			oAuth2Response.getEmail(),
			oAuth2Response.getName(),
			Role.USER)
		);
	}
}
