package Chat;

import Chat.Database.Database;
import Chat.Database.PersistentDB;

import java.util.logging.Logger;

public class Chat {

    public static void Main() {
        try {
            Logger info = Logger.getLogger("INFO");
            Logger error = Logger.getLogger("ERROR");
            Logger debug = Logger.getLogger("DEBUG");

            Database db = new PersistentDB();
        } catch (Exception e) {
            //todo: make multiple catching of exceptions here

        }
    }
}
