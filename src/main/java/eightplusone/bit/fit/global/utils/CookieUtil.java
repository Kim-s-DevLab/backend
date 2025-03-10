package eightplusone.bit.fit.global.utils;

import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public final class CookieUtil {

	public static ResponseCookie createCookie(String name, String value, long cookieExpiration) {
		return ResponseCookie.from(name, value)
			.maxAge(cookieExpiration)
			.path("/")
			.sameSite("Strict")
			.httpOnly(true)
			.build();
	}

	public static Cookie findCookieByName(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		throw new NullPointerException(name + " 쿠키를 찾을 수 없습니다.");
	}
}
