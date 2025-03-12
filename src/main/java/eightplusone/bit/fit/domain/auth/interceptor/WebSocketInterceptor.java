package eightplusone.bit.fit.domain.auth.interceptor;

import static eightplusone.bit.fit.global.constants.TokenConstant.*;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;

import eightplusone.bit.fit.domain.auth.jwt.TokenProvider;
import eightplusone.bit.fit.global.exception.CustomException;
import eightplusone.bit.fit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Configuration
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {

	private final TokenProvider tokenProvider;

	@SneakyThrows
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (accessor.getCommand() == StompCommand.CONNECT) {
			String accessorFirstNativeHeader = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
			String accessToken = accessorFirstNativeHeader.substring(BEARER_PREFIX.length());

			if (!tokenProvider.validateAccessToken(accessToken)) {
				throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
			}
			Authentication authentication = tokenProvider.getAuthenticationByAccessToken(accessToken);
			accessor.setUser(authentication);
		}
		return message;
	}
}