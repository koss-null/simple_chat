package Chat;

import Chat.Database.Database;
import Chat.Database.PersistentDB;
import Chat.Message.Message;
import Chat.User.Type.Type;
import Chat.User.User;

import java.util.logging.Logger;

public class Chat {

    public static void main(String... args) {
        try {
            Logger info = Logger.getLogger("INFO");
            Logger error = Logger.getLogger("ERROR");
            Logger debug = Logger.getLogger("DEBUG");

            Database usersDB = new PersistentDB<String, User>();
            usersDB.init("/Users/d.kossovich/learning/simple_chat/var/users.db");
            Database mailDB = new PersistentDB<String, Message>();
            mailDB.init("/Users/d.kossovich/learning/simple_chat/var/mail.db");

            User u1 = new User("Master", "admin123", Type.ADMIN);
            User u2 = new User("Dima", "admi123", Type.REGULAR);
            User u3 = new User("Sasha", "adm123", Type.REGULAR);
            User u4 = new User("Julia", "ad123", Type.REGULAR);
            User u5 = new User("Yegor", "min123", Type.REGULAR);
            User u6 = new User("Petr", "admin23", Type.REGULAR);
            User u7 = new User("Valys", "adm12", Type.REGULAR);

            usersDB.set(u1.getKey(), u1);
            usersDB.set(u2.getKey(), u2);
            usersDB.set(u3.getKey(), u3);
            usersDB.set(u4.getKey(), u4);
            usersDB.set(u5.getKey(), u5);
            usersDB.set(u6.getKey(), u6);
            usersDB.set(u7.getKey(), u7);

            usersDB.store();
            mailDB.store();
        } catch (Exception e) {
            //todo: make multiple catching of exceptions here
            e.printStackTrace();
        }
    }
}
