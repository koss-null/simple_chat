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

            Database db = new PersistentDB();
            db.init();
            db.set(new String[] {"users", "mike"}, "slon");
            db.set(new String[] {"users", "rob"}, "mish");
            db.set(new String[] {"users", "elthon"}, "kon'");
            db.set(new String[] {"a", "b"}, "vorobey");

            db.store();
            var users = db.getKeys(new String[] {"users"});
            for (String user: users) {
                System.out.println(user);
            }
            var as = db.getKeys(new String[] {"a"});
            for (String user: as) {
                System.out.println(user);
            }
        } catch (Exception e) {
            //todo: make multiple catching of exceptions here

        }
    }
}
