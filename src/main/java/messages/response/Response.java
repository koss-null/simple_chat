package messages.response;

import messages.message.Message;
import messages.responsetype.ResponseType;
import сhat.user.type.Type;

import java.io.Serializable;

public class Response implements Serializable {
    public ResponseType type;
    public Message message;
    public Message[] messageList;
    public boolean ok;
    public String service;
    public String[] chatList;

    public Response() {}

    public Response(ResponseType type) {
        this.type = type;
    }

    public Response(boolean ok) {
        this.type = ResponseType.BOOLEAN_RESPONSE;
        this.ok = ok;
    }

    public Response(String service) {
        this.type = ResponseType.SERVICE_MESSAGE;
        this.service = service;
    }

    public Response(ResponseType type, Message message) {
        this.type = type;
        this.message = message;
    }

    public Response(String[] chatsList) {
        this.type = ResponseType.CHAT_LIST;
        this.chatList = chatsList;
    }

    public Response(Message[] messageList) {
        this.type = ResponseType.MESSAGE_LIST;
        this.messageList = messageList;
    }
}
