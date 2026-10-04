package ro.mpp2026.restclient;

public class EventDTO {
    private long id;
    private String name;
    private int distance;
    private int minAge;
    private int maxAge;

    public EventDTO() {
    }

    public EventDTO(long id, String name, int distance, int minAge, int maxAge) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public long getId() {
        return id;
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

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "EventDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", distance=" + distance +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                '}';
    }
}