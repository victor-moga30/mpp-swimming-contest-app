package ro.mpp2026.model;

public class Registration {
    private long id;
    private long childId;
    private long eventId;

    public Registration() {
    }

    public Registration(long id, long childId, long eventId) {
        this.id = id;
        this.childId = childId;
        this.eventId = eventId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChildId() {
        return childId;
    }

    public void setChildId(long childId) {
        this.childId = childId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "id=" + id +
                ", childId=" + childId +
                ", eventId=" + eventId +
                '}';
    }
}