package сhat.client;

import сhat.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private String serverHost = "127.0.0.1";

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public Client() throws IOException {
        socket = new Socket(serverHost, Server.getPort());
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
    }

    // todo: it's a STUB, need to be changed
    public void send(String msg) throws IOException {
        output.writeUTF(msg);
    }

    public void setServerHost(String host) {
        serverHost = host;
    }
}
