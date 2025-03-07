package eightplusone.bit.fit.domain.auth.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

	private final Map<String, Object> attribute;
	private final String id;
	private final String email;
	private final String nickName;

	public KakaoResponse(Map<String, Object> attribute) {
		Map<String, Object> kakaoAccount = (Map<String, Object>)attribute.get("kakao_account");
		Map<String, Object> kakaoProfile = (Map<String, Object>)attribute.get("properties");
		this.attribute = kakaoProfile;

		this.id = ((Long)attribute.get("id")).toString();
		this.email = (String)kakaoAccount.get("email");

		this.nickName = (String)kakaoProfile.get("nickname");
		this.attribute.put("id", id);
		this.attribute.put("email", this.email);
	}

	@Override
	public String getProvider() {

		return "kakao";
	}

	@Override
	public String getProviderId() {

		return id;
	}

	@Override
	public String getEmail() {

		return email;
	}

	@Override
	public String getName() {

		return nickName;
	}
}
