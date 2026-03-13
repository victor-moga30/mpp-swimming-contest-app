package ro.mpp2026.model;

public class Event {
    private long id;
    private String name;
    private int distance;
    private int minAge;
    private int maxAge;

    public Event() {
    }

    public Event(long id, String name, int distance, int minAge, int maxAge) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isAllowedForAge(int age) {
        return age >= minAge && age <= maxAge;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", distance=" + distance +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                '}';
    }
}