package eightplusone.bit.fit.domain.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import eightplusone.bit.fit.domain.auth.dto.CustomUserDetails;
import eightplusone.bit.fit.domain.user.entity.User;
import eightplusone.bit.fit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public CustomUserDetails loadUserByUsername(String email) {
		User findUser = userRepository.findLoginUserByEmail(email);
		if (findUser != null) {
			return new CustomUserDetails(findUser);
		}
		return null;
	}
}
