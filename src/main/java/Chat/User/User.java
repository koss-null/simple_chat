package Chat.User;

import java.util.UUID;

public class User {
    public String name;
    public String pass;
    public UUID id;

    User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }
}
