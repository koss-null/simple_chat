package Chat.Message;

import Chat.Message.Encryprion.Encriptions;
import Chat.User.User;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    String text;
    Encriptions encriptionType;
    User sender;
    User recipient;
    UUID charId;

    public Message(
            String text,
            Encriptions encriptionType,
            User sender,
            User recipient,
            UUID charId) {

        this.text = text;
        this.encriptionType = encriptionType;
        this.sender = sender;
        this.recipient = recipient;
        this.charId = charId;
    }
}
