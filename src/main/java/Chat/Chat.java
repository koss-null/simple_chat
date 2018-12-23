package Chat;

import Chat.CLI.CLI;
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

            CLI cli = new CLI(usersDB, mailDB);
            cli.start();

            usersDB.store();
            mailDB.store();
        } catch (Exception e) {
            //todo: make multiple catching of exceptions here
            e.printStackTrace();
        }
    }
}
