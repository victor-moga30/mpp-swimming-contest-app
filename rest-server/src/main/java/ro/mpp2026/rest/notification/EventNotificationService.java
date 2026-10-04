package ro.mpp2026.rest.notification;

import org.springframework.stereotype.Service;
import ro.mpp2026.model.Event;
import ro.mpp2026.rest.dto.EventNotification;
import ro.mpp2026.rest.websocket.EventWebSocketHandler;

@Service
public class EventNotificationService {
    private final EventWebSocketHandler eventWebSocketHandler;

    public EventNotificationService(EventWebSocketHandler eventWebSocketHandler) {
        this.eventWebSocketHandler = eventWebSocketHandler;
    }

    public void notifyCreated(Event event, String changedBy) {
        eventWebSocketHandler.sendNotification(
                new EventNotification("CREATED", event, event.getId(), changedBy)
        );
    }

    public void notifyUpdated(Event event, String changedBy) {
        eventWebSocketHandler.sendNotification(
                new EventNotification("UPDATED", event, event.getId(), changedBy)
        );
    }

    public void notifyDeleted(Long eventId, String changedBy) {
        eventWebSocketHandler.sendNotification(
                new EventNotification("DELETED", null, eventId, changedBy)
        );
    }
}