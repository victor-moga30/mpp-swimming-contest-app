package ro.mpp2026.restclient;

public class EventCreateRequest {
    private String name;
    private int distance;
    private int minAge;
    private int maxAge;

    public EventCreateRequest() {
    }

    public EventCreateRequest(String name, int distance, int minAge, int maxAge) {
        this.name = name;
        this.distance = distance;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
}