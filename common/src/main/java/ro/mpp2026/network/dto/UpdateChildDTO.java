package ro.mpp2026.network.dto;

import java.io.Serializable;
import java.util.List;

public class UpdateChildDTO implements Serializable {
    private final String cnp;
    private final List<Long> eventIds;

    public UpdateChildDTO(String cnp, List<Long> eventIds) {
        this.cnp = cnp;
        this.eventIds = eventIds;
    }

    public String getCnp() {
        return cnp;
    }

    public List<Long> getEventIds() {
        return eventIds;
    }
}