package Chat.User;

import Chat.User.Type.Type;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    public String name;
    public String pass;
    public Type type;
    public UUID id;

    public User(String name, String pass, Type type) {
        this.name = name;
        this.pass = pass;
        this.type = type;
        this.id   = UUID.randomUUID();
    }

    public String[] getKey() {
        return new String[]{this.type.toString(), this.id.toString()};
    }
}
