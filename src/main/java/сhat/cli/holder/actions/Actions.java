package сhat.cli.holder.actions;

import сhat.cli.CLI;
import сhat.server.Server;
import сhat.client.Client;
import сhat.database.Database;
import сhat.user.User;

import java.io.IOException;
import java.util.Scanner;

public class Actions {

    private static Client client = null;
    private static Server server = null;

    public static void server(Database userDb, Database mailDb) {
        Server server = new Server(userDb, mailDb);
        server.start();
        CLI.isServer = true;
    }

    public static void client() {
        System.out.println("client was chosen");
        try {
            //todo: need to check this out
            client = new Client();
            CLI.isServer = false;
        } catch (IOException e) {
            System.out.println("there are troubles creating a client, please restart the application");
        }
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

    private static final String QUIT = "::quit";

    public static boolean logOn() {
        Scanner input = new Scanner(System.in);

        System.out.println("Please, enter your (requesttype " + QUIT + " if you decided to quit)");
        System.out.print("Login: ");
        boolean exist = true;
        String login = "";
        while (exist) {
            login = input.next().toLowerCase();
            if (login.equals(QUIT)) {
                return false;
            }
            if (login.length() < 3) {
                System.out.println("login is to short, retry");
                continue;
            }

            exist = client.checkLoginExist(login);
            if (exist) {
                System.out.println("This login is already in use");
            }
        }

        String firstPass, secondPass;
        do {
            System.out.println("Print password:");
            firstPass = input.next();
            if (firstPass.equals(QUIT)) {
                return false;
            }
            System.out.println("Repeat password:");
            secondPass = input.next();
            if (secondPass.equals(QUIT)) {
                return false;
            }
        } while(!firstPass.equals(secondPass));

        var success = client.addUser(login, firstPass);
        if (!success) {
            System.out.println("some troubles have been appeared during registration, please retry");
            return false;
        }
        System.out.println("User " + login + " have been successfully added");
        return true;
    }

    // todo: to be removed
    public static void send() {
        System.out.println("enter your message");

        Scanner input = new Scanner(System.in);
        var msg = input.nextLine();
        try {
            client.send(msg);
        } catch (IOException e) {
            System.out.println("OOPS, failed");
        }
    }
}
