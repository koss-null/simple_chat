package messages.message;

import messages.message.encryption.Encryption;
import сhat.user.User;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    public String text;
    public Encryption encriptionType;
    public User sender;
    public User recipient;
    public UUID groupId;

    public static String PrivateKey = "private";
    public static String ChatKey = "chat";

    public Message(
            String text,
            Encryption encryptionType,
            User sender,
            User recipient,
            UUID groupId) {

        this.text = text;
        this.encriptionType = encryptionType;
        this.sender = sender;
        this.recipient = recipient;
        this.groupId = groupId;
    }

    public String[] getKey() {
        if (this.recipient != null) {
            return new String[]{PrivateKey, this.recipient.id.toString()};
        }
        return new String[]{ChatKey, this.groupId.toString()};
    }
}
