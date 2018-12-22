package Chat.Server.Handler;

import Chat.Server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class Handler implements Runnable {
    final DataInputStream input;
    final DataOutputStream output;
    UUID id;

    public Handler(Socket socket) throws IOException {
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        boolean done = false;
        while (!done) {
            try {
                String message = input.readUTF();
                System.out.println("got msg: " + message);

                for (var client : Server.getActiveClients()) {
                    // here we'r sending message to a client with the id in the recipients
                }
            } catch (IOException e) {
                // todo: fixit
                e.printStackTrace();
                done = true;
            }

        }
    }
}