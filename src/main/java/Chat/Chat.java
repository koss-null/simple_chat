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
            db.set(new String[] {"a"}, "slon");
            db.set(new String[] {"b"}, "mish");
            db.set(new String[] {"c"}, "kon'");
            db.set(new String[] {"a", "b"}, "vorobey");

            db.store();

            db.init();
            System.out.println(db.get(new String[] {"a"}));
            System.out.println(db.get(new String[] {"a", "b"}));
            System.out.println(db.get(new String[] {"a", "c"}));
        } catch (Exception e) {
            //todo: make multiple catching of exceptions here

        }
    }
}
