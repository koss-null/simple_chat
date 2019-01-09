package сhat.cli.holder.actions;

import сhat.cli.CLI;
import сhat.server.Server;
import сhat.client.Client;
import сhat.database.Database;
import сhat.user.User;

import java.io.IOException;
import java.util.Scanner;

public class Actions {

    public static void server() throws IOException {
        Server server = new Server();
        server.start();
        CLI.isServer = true;
    }

    public static void client() {
        System.out.println("client was chosen");
    }

    public static User login(Database users) throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("Please, enter your");
        System.out.print("Login: ");
        var name = input.next();
        System.out.print("Password: ");
        var pass = input.next();

        Object ks = users.getKeys(new String[]{"users"});
        if (ks == null) {
            System.out.println("There are no created users yet");
            return null;
        }
        String[] keys = (String[]) ks;

        User user = null;
        for (String key: keys) {
            var u = (User) users.get(new String[] {"users", key});
            if (u.name.equals(name) && u.pass.equals(pass)) {
                user = u;
                break;
            }
        }

        // todo : client need to be initialised somewhere else
        Client client = new Client();

        return user;
    }
}
