package ro.mpp2026.network.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private final String username;
    private final String password;
    private final long id;
    private final String office;

    public UserDTO(String username, String password) {
        this(username, password, 0, null);
    }

    public UserDTO(long id, String username, String office) {
        this(username, null, id, office);
    }

    public UserDTO(String username, String password, long id, String office) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.office = office;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public String getOffice() {
        return office;
    }
}