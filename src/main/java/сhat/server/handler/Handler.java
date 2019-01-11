package сhat.server.handler;

import сhat.database.Database;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Handler extends Thread {
    private static final int MAX_MSG_LENGTH = 1024;

    private final DataInputStream input;
    private final DataOutputStream output;

    private final Database userDb;
    private final Database mailDb;

    public Handler(Socket socket, Database userDb, Database mailDb) throws IOException {
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());

        this.userDb = userDb;
        this.mailDb = mailDb;
    }

    @Override
    public void run() {
        System.out.println("Started a new handler, waiting a client message");

        boolean done = false;
        while (!done) {
            byte[] msgBytes = new byte[MAX_MSG_LENGTH];
            try {
                int length = input.read(msgBytes);
                var message = Arrays.copyOfRange(msgBytes, 0, length);
                System.out.println("got msg: " + new String(message, StandardCharsets.UTF_8));
            } catch (IOException e) {
                System.out.println("Some troubles with the server, please restart " +
                        "or contact with the product owner to fix the problem");
                e.printStackTrace();
                done = true;
            }

        }
    }
}