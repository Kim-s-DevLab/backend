package eightplusone.bit.fit.domain.mysession.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eightplusone.bit.fit.domain.mysession.dto.MySessionRegisterRequestDto;
import eightplusone.bit.fit.domain.mysession.service.MySessionService;
import eightplusone.bit.fit.global.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/sessions")
public class MySessionController {

	private final MySessionService mySessionService;

	@Operation(summary = "세션 미리 담기 요청", description = "**성공 응답 데이터:**  null")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "세션 미리 담기 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
		@ApiResponse(responseCode = "401", description = "유효한 토큰이 아닙니다."),
		@ApiResponse(responseCode = "404", description = "해당 세션을 찾을 수 없습니다."),
		@ApiResponse(responseCode = "500", description = "서버에 오류가 발생했습니다."),
	})
	@PostMapping
	public ResponseEntity<ResponseDto<Object>> register(
		@RequestBody MySessionRegisterRequestDto mySessionRegisterRequestDto) {
		mySessionService.registerMySession(SecurityContextHolder.getContext().getAuthentication().getName(),
			mySessionRegisterRequestDto.getSessionId());
		return ResponseEntity.status(CREATED).body(ResponseDto.success(CREATED, "세션 미리 담기 등록 성공", null));
	}
}
