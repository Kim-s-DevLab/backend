package eightplusone.bit.fit.domain.user.controller;

import static eightplusone.bit.fit.global.constants.TokenConstant.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightplusone.bit.fit.domain.user.service.UserService;
import eightplusone.bit.fit.global.utils.CookieUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@DeleteMapping
	public ResponseEntity<?> delete() {
		userService.delete();
		return ResponseEntity.status(CREATED)
			.header(HttpHeaders.SET_COOKIE,
				CookieUtil.createCookie(REFRESH_TOKEN_COOKIE_NAME, null, REFRESH_EXPIRATION_DELETE).toString())
			.body(null);
	}
}
