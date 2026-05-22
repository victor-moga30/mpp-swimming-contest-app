package ro.mpp2026.rest.websocket;

import java.security.Principal;

public class WebSocketUserPrincipal implements Principal {
    private final String name;

    public WebSocketUserPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}