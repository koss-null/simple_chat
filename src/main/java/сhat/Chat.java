package сhat;

import сhat.cli.CLI;
import сhat.database.Database;
import сhat.database.PersistentDB;
import messages.message.Message;
import сhat.user.User;

public class Chat {

    public static void main(String... args) {
        try {
            Database usersDB = new PersistentDB<String, User>();
            usersDB.init("./var/users.db");
            Database mailDB = new PersistentDB<String, Message>();
            mailDB.init("./var/mail.db");

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
