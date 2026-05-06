package ro.mpp2026.network.objectprotocol;

import java.io.Serializable;

public class Response implements Serializable {
    private final ResponseType type;
    private final Object data;

    public Response(ResponseType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public ResponseType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}