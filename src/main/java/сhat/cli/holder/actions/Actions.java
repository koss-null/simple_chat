package сhat.cli.holder.actions;

import сhat.cli.CLI;
import сhat.server.Server;
import сhat.client.Client;
import сhat.database.Database;
import сhat.user.User;

import java.io.IOException;
import java.util.Scanner;

public class Actions {
    public enum LoginState{
        OK, FAILED
    }

    public static void server() throws IOException {
        Server server = new Server();
        server.start();
        CLI.isServer = true;
    }

    public static void client() {
        System.out.println("client was chosen");
    }

    public static LoginState login(Database users) throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("Please, enter your");
        System.out.print("Login: ");
        var name = input.next();
        System.out.print("Password: ");
        var pass = input.next();

        Object ks = users.getKeys(new String[]{"users"});
        if (ks == null) {
            System.out.println("There are no created users yet");
            return LoginState.FAILED;
        }
        String[] keys = (String[]) ks;

        var logOk = false;
        User user;
        for (String key: keys) {
            var u = (User) users.get(new String[] {"users", key});
            if (u.name.equals(name) && u.pass.equals(pass)) {
                logOk = true;
                user = u;
                break;
            }
        }

        if (!logOk) { return LoginState.FAILED; }
        Client client = new Client();

        return LoginState.OK;
    }
}
