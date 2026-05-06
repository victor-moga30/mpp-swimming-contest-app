package ro.mpp2026.service.dto;

import java.io.Serializable;

public class ChildDTO implements Serializable {
    private long id;
    private String name;
    private String cnp;
    private int age;

    public ChildDTO(long id, String name, String cnp, int age) {
        this.id = id;
        this.name = name;
        this.cnp = cnp;
        this.age = age;
    }

    public long getId() {
        return id;
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
}