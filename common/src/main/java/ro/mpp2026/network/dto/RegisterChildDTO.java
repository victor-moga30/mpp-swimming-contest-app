package ro.mpp2026.network.dto;

import java.io.Serializable;
import java.util.List;

public class RegisterChildDTO implements Serializable {
    private final String name;
    private final String cnp;
    private final int age;
    private final List<Long> eventIds;

    public RegisterChildDTO(String name, String cnp, int age, List<Long> eventIds) {
        this.name = name;
        this.cnp = cnp;
        this.age = age;
        this.eventIds = eventIds;
    }

    public String getName() {
        return name;
    }

    public String getCnp() {
        return cnp;
    }

    public int getAge() {
        return age;
    }

    public List<Long> getEventIds() {
        return eventIds;
    }
}