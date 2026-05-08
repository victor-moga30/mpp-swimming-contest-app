package ro.mpp2026.model.normal;

public class User {
    private long id;
    private String username;
    private String passwordHash;
    private String office;

    public User() {
    }

    public User(long id, String username, String passwordHash, String office) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.office = office;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", office='" + office + '\'' +
                '}';
    }
}