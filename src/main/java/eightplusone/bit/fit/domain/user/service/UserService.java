package eightplusone.bit.fit.domain.user.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eightplusone.bit.fit.domain.auth.service.OAuth2UnlinkService;
import eightplusone.bit.fit.domain.auth.service.RedisTokenService;
import eightplusone.bit.fit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final RedisTokenService redisTokenService;
	private final OAuth2UnlinkService oAuth2UnlinkService;

	@Transactional
	public void delete() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		userRepository.deleteByEmail(email);
		deleteOauth2AccessToken(email);
	}

	private void deleteOauth2AccessToken(String email) {
		if (email.startsWith("kakao") || email.startsWith("google") || email.startsWith("naver")) {
			oAuth2UnlinkService.unlink(email);
		}
		if (redisTokenService.getOauth2AccessToken(email) != null) {
			redisTokenService.deleteOauth2AccessToken(email);
		}
	}
}
