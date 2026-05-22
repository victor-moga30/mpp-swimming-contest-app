package ro.mpp2026.rest.websocket;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;
import ro.mpp2026.model.User;
import ro.mpp2026.repository.UserRepository;
import ro.mpp2026.repository.hibernate.UserHibernateRepository;
import ro.mpp2026.rest.security.JwtTokenUtil;

import java.util.List;
import java.util.Map;

@Component
public class JwtWebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    public JwtWebSocketHandshakeInterceptor(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = new UserHibernateRepository();
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler webSocketHandler,
                                   Map<String, Object> attributes) {
        List<String> tokens = UriComponentsBuilder
                .fromUri(request.getURI())
                .build()
                .getQueryParams()
                .get("token");

        if (tokens == null || tokens.isEmpty()) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        String token = tokens.get(0);

        if (!jwtTokenUtil.validateToken(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username);

        if (user == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        attributes.put("username", username);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler webSocketHandler,
                               Exception exception) {
    }
}