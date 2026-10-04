package ro.mpp2026.rest.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ro.mpp2026.rest.dto.EventNotification;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EventWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessionsById = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Object username = session.getAttributes().get("username");

        if (username == null) {
            try {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Client neautentificat."));
            } catch (IOException exception) {
                System.err.println("Eroare la inchiderea conexiunii websocket: " + exception.getMessage());
            }
            return;
        }

        sessionsById.put(session.getId(), session);
        System.out.println("WebSocket autentificat: " + username + ", sessionId=" + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionsById.remove(session.getId());
        System.out.println("WebSocket inchis: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Mesaj primit prin WebSocket: " + message.getPayload());
    }

    public void sendNotification(EventNotification notification) {
        try {
            String json = objectMapper.writeValueAsString(notification);
            TextMessage message = new TextMessage(json);

            for (WebSocketSession session : sessionsById.values()) {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            }
        } catch (Exception exception) {
            System.err.println("Eroare la trimiterea notificarii websocket: " + exception.getMessage());
        }
    }
}