package ro.mpp2026.model.normal;

public class Child {
    private long id;
    private String name;
    private String cnp;
    private int age;

    public Child() {
    }

    public Child(long id, String name, String cnp, int age) {
        this.id = id;
        this.name = name;
        this.cnp = cnp;
        this.age = age;
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

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Child{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cnp='" + cnp + '\'' +
                ", age=" + age +
                '}';
    }
}