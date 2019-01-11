package messages.response;

import messages.message.Message;
import messages.responsetype.ResponseType;
import сhat.user.type.Type;

import java.io.Serializable;

public class Response implements Serializable {
    public ResponseType type;
    public Message message;
    public boolean ok;
    public String service;

    public Response(ResponseType type) {
        this.type = type;
    }

    public Response(ResponseType type, boolean ok) {
        this.type = type;
        this.ok = ok;
    }

    public Response(ResponseType type, String service) {
        this.type = type;
        this.service = service;
    }

    public Response(ResponseType type, Message message) {
        this.type = type;
        this.message = message;
    }
}
