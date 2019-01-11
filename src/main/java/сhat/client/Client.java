package сhat.client;

import сhat.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
        output.write(msg.getBytes(StandardCharsets.UTF_8));
    }

    // returns true if exist
    public boolean checkLoginExist(String login) {
        // fixme
        return false;
    }

    public boolean addUser(String login, String pass) {
        // fixme
        return true;
    }

    public void setServerHost(String host) {
        serverHost = host;
    }
}
