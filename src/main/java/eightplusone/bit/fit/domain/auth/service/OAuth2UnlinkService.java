package eightplusone.bit.fit.domain.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2UnlinkService {

	private static final String GOOGLE_URL = "https://oauth2.googleapis.com/revoke";
	private static final String KAKAO_URL = "https://kapi.kakao.com/v1/user/unlink";
	private static final String NAVER_URL = "https://nid.naver.com/oauth2.0/token";

	private final RestTemplate restTemplate = new RestTemplate();
	private final RedisTokenService redisTokenService;

	@Value("${spring.security.oauth2.client.registration.naver.client-id}")
	private static String NAVER_CLIENT_ID;
	@Value("${spring.security.oauth2.client.registration.naver.client-secret}")
	private static String NAVER_CLIENT_SECRET;

	public void unlink(String provider) {
		if (provider.startsWith("google")) {
			googleUnlink(provider);
		} else if (provider.startsWith("kakao")) {
			kakaoUnlink(provider);
		} else if (provider.startsWith("naver")) {
			naverUnlink(provider);
		} else {
			throw new IllegalStateException("허용되지 않은 소셜 로그인 제공자");
		}
	}
	
	private void googleUnlink(String provider) {
		String accessToken = redisTokenService.getOauth2AccessToken(provider);
		// oauth2 토큰이 만료 시 재 로그인
		if (accessToken == null) {
			throw new IllegalStateException("소셜 로그인 토큰 만료, 재로그인 필요");
		}
		// 바디 설정
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("token", accessToken);
		restTemplate.postForObject(GOOGLE_URL, params, String.class);
	}

	private void kakaoUnlink(String provider) {
		String accessToken = redisTokenService.getOauth2AccessToken(provider);
		// oauth2 토큰이 만료 시 재 로그인
		if (accessToken == null) {
			throw new IllegalStateException("소셜 로그인 토큰 만료, 재로그인 필요");
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<Object> entity = new HttpEntity<>("", headers);
		restTemplate.exchange(KAKAO_URL, HttpMethod.POST, entity, String.class);
	}

	private void naverUnlink(String provider) {

		String accessToken = redisTokenService.getOauth2AccessToken(provider);

		if (accessToken == null) {
			throw new IllegalStateException("소셜 로그인 토큰 만료, 재로그인 필요");
		}

		String url = NAVER_URL
			+ "?service_provider=NAVER"
			+ "&grant_type=delete"
			+ "&client_id="
			+ NAVER_CLIENT_ID
			+ "&client_secret="
			+ NAVER_CLIENT_SECRET
			+ "&access_token="
			+ accessToken;

		NaverUnlinkResponse response = restTemplate.getForObject(url, NaverUnlinkResponse.class);

		if (response != null && !"success".equalsIgnoreCase(response.getResult())) {
			throw new IllegalStateException("소셜 로그인 연결 끊기 실패");
		}
	}

	@Getter
	@RequiredArgsConstructor
	public static class NaverUnlinkResponse {
		@JsonProperty("access_token")
		private final String accessToken;
		private final String result;
	}
}
