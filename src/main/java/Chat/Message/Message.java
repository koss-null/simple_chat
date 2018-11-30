package Chat.Message;

import Chat.Message.Encryprion.Encryption;
import Chat.User.User;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    String text;
    Encryption encriptionType;
    User sender;
    User recipient;
    UUID chatId;

    public Message(
            String text,
            Encryption encriptionType,
            User sender,
            User recipient,
            UUID chatId) {

        this.text = text;
        this.encriptionType = encriptionType;
        this.sender = sender;
        this.recipient = recipient;
        this.chatId = chatId;
    }
}
