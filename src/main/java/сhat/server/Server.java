package сhat.server;

import сhat.database.Database;
import сhat.server.handler.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    private static int Port = 9090;

    private final static List<Handler> activeClients = new ArrayList<>();
    static public List<Handler> getActiveClients() {
        return activeClients;
    }

    private final Database userDb;
    private final Database mailDb;

    public Server(Database userDb, Database mailDb) {
        System.out.println("initialising server for DEFAULT port: " + Port);
        this.userDb = userDb;
        this.mailDb = mailDb;
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(Server.Port)) {
            boolean done = false;
            while (!done) {
                try {
                    Handler handler = new Handler(server.accept(), userDb, mailDb);
                    Thread t = new Thread(handler);
                    t.start();
                    activeClients.add(handler);
                } catch (IOException e) {
                    System.out.println("Some handler have been broken");
                    // done = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Server failed, please restart or connect to a project owner to fix the problem");
            e.printStackTrace();
        }
    }

    public static void setPort(int port) {
        Server.Port = port;
    }

    public static int getPort() {
        return Port;
    }
}
