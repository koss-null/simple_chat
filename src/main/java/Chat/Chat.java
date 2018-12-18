package Chat;

import Chat.Database.Database;
import Chat.Database.PersistentDB;

import java.util.logging.Logger;

public class Chat {

    public static void main(String... args) {
        try {
            Logger info = Logger.getLogger("INFO");
            Logger error = Logger.getLogger("ERROR");
            Logger debug = Logger.getLogger("DEBUG");

            Database db = new PersistentDB<String, String>();
            db.init("/Users/d.kossovich/learning/simple_chat/var/persistent.db");
            var users = (String[]) db.getKeys(new String[] {"users"});

            db.store();

            for (String user: users) {
                System.out.println(user);
            }
        } catch (Exception e) {
            //todo: make multiple catching of exceptions here

        }
    }
}
