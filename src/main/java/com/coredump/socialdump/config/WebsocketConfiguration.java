package com.coredump.socialdump.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;



@EnableScheduling
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {


  private final Logger log = LoggerFactory.getLogger(WebsocketConfiguration.class);

  public static final String IP_ADDRESS = "IP_ADDRESS";

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
//		registry.enableStompBrokerRelay("/queue/", "/topic/");
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("websocket/eventposts")
          .withSockJS();

    //Currently not using
    /*registry.addEndpoint("/websocket/tracker")
          .setHandshakeHandler(new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
              Principal principal = request.getPrincipal();

              if (principal == null) {
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
                principal = new AnonymousAuthenticationToken("WebsocketConfiguration", "anonymous", authorities);
              }

              return principal;
            }
          })
          .withSockJS()
          .setInterceptors(httpSessionHandshakeInterceptor());
    */
  }

  //Currently not using
  /*
  @Bean
  public HandshakeInterceptor httpSessionHandshakeInterceptor() {

    return new HandshakeInterceptor() {

      @Override
      public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
          ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
          attributes.put(IP_ADDRESS, servletRequest.getRemoteAddress());
        }
        return true;
      }

      @Override
      public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

      }
    };

  }*/
}
