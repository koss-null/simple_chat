package Chat.CLI.Holder.Actions;

import Chat.CLI.CLI;
import Chat.Server.Server;
import Chat.Client.Client;
import Chat.Database.Database;
import Chat.User.User;

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
        System.out.println("Client was chosen");
    }

    public static LoginState login(Database users) throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("Please, enter your");
        System.out.print("Login: ");
        var name = input.next();
        System.out.print("Password: ");
        var pass = input.next();

        String[] keys = (String[]) users.getKeys(new String[]{"users"});
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
