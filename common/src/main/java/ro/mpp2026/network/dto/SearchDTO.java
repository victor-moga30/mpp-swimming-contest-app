package ro.mpp2026.network.dto;

import java.io.Serializable;

public class SearchDTO implements Serializable {
    private final long eventId;
    private final int minAge;
    private final int maxAge;

    public SearchDTO(long eventId, int minAge, int maxAge) {
        this.eventId = eventId;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public long getEventId() {
        return eventId;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }
}