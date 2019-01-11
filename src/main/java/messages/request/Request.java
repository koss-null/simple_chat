package messages.request;

import messages.message.Message;
import messages.requesttype.RequestType;

import java.io.Serializable;

public class Request implements Serializable {
    public RequestType type;
    public Message message;

    public Request(RequestType type) {
        this.type = type;
    }

    public Request(RequestType type, Message message) {
        this.type = type;
        this.message = message;
    }
}
