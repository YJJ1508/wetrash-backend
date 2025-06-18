package yjj.wetrash.domain.chat.config.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import yjj.wetrash.global.security.jwt.JwtTokenProvider;

@Component
@RequiredArgsConstructor
public class ChatJwtChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel){
        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);
        if(accessor.getCommand() == StompCommand.CONNECT){
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null & token.startsWith("Bearer ")){
                token = token.substring(7);
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                System.out.println("authentication: "+authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                accessor.setUser(authentication);
            }
        }
        return message;
    }
}
