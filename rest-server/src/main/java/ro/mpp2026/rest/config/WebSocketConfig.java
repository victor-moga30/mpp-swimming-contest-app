package ro.mpp2026.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ro.mpp2026.rest.websocket.EventWebSocketHandler;
import ro.mpp2026.rest.websocket.JwtWebSocketHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final EventWebSocketHandler eventWebSocketHandler;
    private final JwtWebSocketHandshakeInterceptor jwtWebSocketHandshakeInterceptor;

    public WebSocketConfig(EventWebSocketHandler eventWebSocketHandler,
                           JwtWebSocketHandshakeInterceptor jwtWebSocketHandshakeInterceptor) {
        this.eventWebSocketHandler = eventWebSocketHandler;
        this.jwtWebSocketHandshakeInterceptor = jwtWebSocketHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(eventWebSocketHandler, "/ws/events")
                .addInterceptors(jwtWebSocketHandshakeInterceptor)
                .setAllowedOrigins("http://localhost:5173");
    }
}