package eightplusone.bit.fit.global.config;

import static eightplusone.bit.fit.global.constants.CorsConstant.*;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import eightplusone.bit.fit.domain.auth.filter.JwtAuthenticationFilter;
import eightplusone.bit.fit.domain.auth.handler.CustomOAuth2SuccessHandler;
import eightplusone.bit.fit.domain.auth.jwt.TokenProvider;
import eightplusone.bit.fit.domain.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Value("${cors.allow.origins}")
	private static String allowedOrigins;

	private final TokenProvider tokenProvider;
	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
				CorsConfiguration configuration = new CorsConfiguration();
				configuration.setAllowedOrigins(Collections.singletonList(allowedOrigins));
				configuration.setAllowedMethods(Collections.singletonList(ALLOWED_METHODS));
				configuration.setAllowCredentials(ALLOWED_CREDENTIALS);
				configuration.setAllowedHeaders(Collections.singletonList(ALLOWED_HEADERS));
				configuration.setMaxAge(MAX_AGE);
				configuration.setExposedHeaders(Collections.singletonList(HttpHeaders.SET_COOKIE));
				configuration.setExposedHeaders(Collections.singletonList(HttpHeaders.AUTHORIZATION));
				return configuration;
			}))
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll() // 모든 요청 허용
			)
			.addFilterAfter(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
			.oauth2Login((oauth2) -> oauth2.userInfoEndpoint(
					(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService)))
				.successHandler(new CustomOAuth2SuccessHandler(tokenProvider))
			);

		return http.build();
	}
}
