package ro.mpp2026.rest.dto;

import ro.mpp2026.model.Event;

public class EventNotification {
    private String type;
    private Event event;
    private Long eventId;
    private String changedBy;

    public EventNotification() {
    }

    public EventNotification(String type, Event event, Long eventId, String changedBy) {
        this.type = type;
        this.event = event;
        this.eventId = eventId;
        this.changedBy = changedBy;
    }

    public String getType() {
        return type;
    }

    public Event getEvent() {
        return event;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }
}