package ro.mpp2026.service.dto;

public class ChildRegistrationDTO {
    private long childId;
    private String childName;
    private String cnp;
    private int age;
    private String events;

    public ChildRegistrationDTO(long childId, String childName, String cnp, int age, String events) {
        this.childId = childId;
        this.childName = childName;
        this.cnp = cnp;
        this.age = age;
        this.events = events;
    }

    public long getChildId() {
        return childId;
    }

    public String getChildName() {
        return childName;
    }

    public String getCnp() {
        return cnp;
    }

    public int getAge() {
        return age;
    }

    public String getEvents() {
        return events;
    }

    @Override
    public String toString() {
        return "ChildRegistrationDTO{" +
                "childId=" + childId +
                ", childName='" + childName + '\'' +
                ", cnp='" + cnp + '\'' +
                ", age=" + age +
                ", events='" + events + '\'' +
                '}';
    }
}