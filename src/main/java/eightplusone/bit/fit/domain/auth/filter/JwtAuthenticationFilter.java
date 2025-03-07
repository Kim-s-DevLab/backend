package eightplusone.bit.fit.domain.auth.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import eightplusone.bit.fit.domain.auth.jwt.TokenProvider;
import io.jsonwebtoken.IncorrectClaimException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		/* OAuth2 재로그인 무한 루프 방지*/
		String requestUri = request.getRequestURI();
		if (requestUri.matches("^\\/login(?:\\/.*)?$")) {
			filterChain.doFilter(request, response);
			return;
		}
		if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
			filterChain.doFilter(request, response);
			return;
		}

		String accessToken = tokenProvider.resolveAccessToken(request);

		try {
			if (accessToken != null && tokenProvider.validateAccessToken(accessToken)) {
				Authentication authentication = tokenProvider.getAuthenticationByAccessToken(accessToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (IncorrectClaimException e) {
			SecurityContextHolder.clearContext();
			log.debug("잘못된 토큰 입니다.");
			response.sendError(403);
		} catch (UsernameNotFoundException e) {
			SecurityContextHolder.clearContext();
			log.debug("회원을 찾을 수 없습니다..");
			response.sendError(403);
		}
		filterChain.doFilter(request, response);
	}

	protected boolean shouldNotFilter(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		return requestUri.equals("/reissue"); // TODO: API 스펙에 맞게 수정 할 것
	}
}
