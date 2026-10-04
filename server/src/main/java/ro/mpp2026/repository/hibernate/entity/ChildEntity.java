package ro.mpp2026.repository.hibernate.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "children")
public class ChildEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cnp")
    private String cnp;

    @Column(name = "age")
    private int age;

    public ChildEntity() {
    }

    public ChildEntity(Long id, String name, String cnp, int age) {
        this.id = id;
        this.name = name;
        this.cnp = cnp;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}