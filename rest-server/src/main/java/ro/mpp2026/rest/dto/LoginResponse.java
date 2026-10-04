package ro.mpp2026.rest.dto;

public class LoginResponse {
    private String token;
    private String username;
    private String office;

    public LoginResponse() {
    }

    public LoginResponse(String token, String username, String office) {
        this.token = token;
        this.username = username;
        this.office = office;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getOffice() {
        return office;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setOffice(String office) {
        this.office = office;
    }
}