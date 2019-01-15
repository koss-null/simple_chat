package сhat.client;

import messages.message.Message;
import messages.message.encryption.Encryption;
import messages.request.Request;
import messages.response.Response;
import сhat.server.Server;
import сhat.user.User;
import сhat.user.type.Type;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static messages.requesttype.RequestType.*;
import static messages.responsetype.ResponseType.INCOMING_MESSAGE;
import static messages.responsetype.ResponseType.SERVICE_MESSAGE;

/**
 * client performs an interaction with the server using the following protocol:
 * both sides have the Request class which is serialised and sent to a server
 * where it's deserialized and Response class comes back
 */
public class Client {
    private String serverHost = "127.0.0.1";

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Client() throws IOException {
        socket = new Socket(serverHost, Server.getPort());
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
    }

    // todo: it's a STUB, need to be changed
    public void send(String msg) throws IOException {
        output.write(msg.getBytes(StandardCharsets.UTF_8));
    }

    // returns true if exist
    public boolean checkLoginExist(String login) {
        Request req = new Request(LOGIN_CHECK);
        req.message = new Message(
            "",
            Encryption.NONE,
            new User(login, "", Type.REGULAR),
            null,
            null
        );
        try {
            output.writeObject(req);
        } catch (IOException e) {
            // todo handle
            System.out.println("can't create an object output stream");
        }

        try {
            var resp = (Response) input.readObject();
            return resp.ok;
        } catch (IOException e) {
            // todo handle
            System.out.println("can't create an object output stream");
        } catch (ClassNotFoundException e) {
            // todo handle
            System.out.println("deserialization error");
        }

        return false;
    }

    public User checkRegistration(String login, String pass) {
        Request req = new Request(REGISTER_CHECK);
        req.message = new Message(
                "",
                Encryption.NONE,
                new User(login, pass, Type.REGULAR),
                null,
                null
        );
        try {
            output.writeObject(req);
        } catch (IOException e) {
            // todo handle
            System.out.println("can't create an object output stream");
        }

        try {
            var resp = (Response) input.readObject();
            if (resp.type == INCOMING_MESSAGE) {
                return resp.message.sender;
            } else if (resp.type == SERVICE_MESSAGE) {
                System.out.println(resp.service);
                return null;
            }
            return null;
        } catch (IOException e) {
            // todo handle
            System.out.println("can't create an object output stream");
        } catch (ClassNotFoundException e) {
            // todo handle
            System.out.println("deserialization error");
        }

        return null;
    }

    public boolean addUser(String login, String pass) {
        Request req = new Request(LOGIN_REGISTER);
        req.message = new Message(
                "",
                Encryption.NONE,
                new User(login, pass, Type.REGULAR),
                null,
                null
        );
        try (ObjectOutputStream out = new ObjectOutputStream(output)) {
            out.writeObject(req);
        } catch (IOException e) {
            // todo handle
            System.out.println("can't create an object output stream");
        }

        try (ObjectInputStream in = new ObjectInputStream(input)) {
            var resp = (Response)in.readObject();
            return resp.ok;
        } catch (IOException e) {
            // todo handle
            System.out.println("can't create an object output stream");
        } catch (ClassNotFoundException e) {
            // todo handle
            System.out.println("deserialization error");
        }

        return false;
    }

    public void setServerHost(String host) {
        serverHost = host;
    }
}
