package Chat.Server;

import Chat.Server.Handler.Handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static int Port = 999;

    private final static List<Handler> activeClients = new ArrayList<>();
    static public List<Handler> getActiveClients() {
        return activeClients;
    }

    public void start() throws IOException {
        ServerSocket server = new ServerSocket(Server.Port);

        boolean done = false;
        while (!done) {
            try {
                Socket request = server.accept();
                Handler handler = new Handler(request);

                Thread t = new Thread(handler);
                activeClients.add(handler);
                t.start();
            } catch (IOException e) {
                //todo: fixit
                e.printStackTrace();
                done = true;
            }
        }
    }

    public static void setPort(int port) {
        Server.Port = port;
    }

    public static int getPort() {
        return Port;
    }
}
