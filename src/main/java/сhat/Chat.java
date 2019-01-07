package сhat;

import сhat.cli.CLI;
import сhat.database.Database;
import сhat.database.PersistentDB;
import сhat.message.Message;
import сhat.user.User;

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
