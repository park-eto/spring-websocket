package com.spring.websocket.config;

import com.spring.websocket.custom.WebSocketSessionCapturingHandlerDecorator;
import com.spring.websocket.interceptors.HttpSessionIdHandshakeInterceptor;
import com.spring.websocket.interceptors.SessionKeepAliveChannelInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

/**
 * 웹소켓 설정
 * @EnableWebSocketMessageBroker
 * - 메시지 중개인에 의해 웹 소켓 메시지 처리를 할 수 있다
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    /**
     * configureMessageBroker
     * 메시지 중개인의 구성인 WebSocketMessageBrokerConfigurer 를 재구성하기 위한 것이다
     */

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.enableSimpleBroker("/topic/","queue/");
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * registerStompEndpoints : SockJS에 접속을 시도할 수 있게 해줍니다
     * STOMP 통신을 하는 곳인가
     */

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS().setInterceptors(httpSessionIdHandshakeInterceptor());
    }

    /**
     * 핸드쉐이크하기 전, 후 설정
     */

    @Bean
    public HttpSessionIdHandshakeInterceptor httpSessionIdHandshakeInterceptor() {
        return new HttpSessionIdHandshakeInterceptor();
    }


    /**
     * Inbound 서버가 클라이언트로 부터 채널을 받는것
     * 인터셉터 등록
     */

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.setInterceptors(sessionKeepAliveChannelInterceptor());
    }

    /**
     * 세션과 그 ID를 유지하기 위한 인터셉터
     */
    @Bean
    public SessionKeepAliveChannelInterceptor sessionKeepAliveChannelInterceptor() {
        return new SessionKeepAliveChannelInterceptor();
    }

    /**
     *
     * 웹소켓 전송 재정의 ~???
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(WebSocketHandler webSocketHandler) {
                return new WebSocketSessionCapturingHandlerDecorator(webSocketHandler);
            }
        });
    }
}
