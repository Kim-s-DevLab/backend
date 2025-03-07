package eightplusone.bit.fit.domain.auth.dto;

public interface OAuth2Response {

	String getProvider();

	String getProviderId();

	String getEmail();

	String getName();
}
