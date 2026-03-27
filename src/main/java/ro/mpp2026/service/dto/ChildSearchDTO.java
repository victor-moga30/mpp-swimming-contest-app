package ro.mpp2026.service.dto;

public class ChildSearchDTO {
    private long childId;
    private String childName;
    private int age;
    private int registeredEventsCount;

    public ChildSearchDTO(long childId, String childName, int age, int registeredEventsCount) {
        this.childId = childId;
        this.childName = childName;
        this.age = age;
        this.registeredEventsCount = registeredEventsCount;
    }

    public long getChildId() {
        return childId;
    }

    public String getChildName() {
        return childName;
    }

    public int getAge() {
        return age;
    }

    public int getRegisteredEventsCount() {
        return registeredEventsCount;
    }

    @Override
    public String toString() {
        return "ChildSearchDTO{" +
                "childId=" + childId +
                ", childName='" + childName + '\'' +
                ", age=" + age +
                ", registeredEventsCount=" + registeredEventsCount +
                '}';
    }
}