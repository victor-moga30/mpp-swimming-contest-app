package ro.mpp2026.service.dto;

import java.io.Serializable;

public class EventParticipantsDTO implements Serializable {
    private long eventId;
    private String eventName;
    private int distance;
    private int minAge;
    private int maxAge;
    private int participantsCount;

    public EventParticipantsDTO(long eventId, String eventName, int distance, int minAge, int maxAge, int participantsCount) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.distance = distance;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.participantsCount = participantsCount;
    }

    public long getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public int getDistance() {
        return distance;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getParticipantsCount() {
        return participantsCount;
    }

    @Override
    public String toString() {
        return "EventParticipantsDTO{" +
                "eventId=" + eventId +
                ", eventName='" + eventName + '\'' +
                ", distance=" + distance +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                ", participantsCount=" + participantsCount +
                '}';
    }
}