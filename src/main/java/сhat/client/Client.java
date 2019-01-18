package сhat.client;

import messages.message.Message;
import messages.message.encryption.Encryption;
import messages.request.Request;
import messages.response.Response;
import messages.responsetype.ResponseType;
import сhat.client.pushhandler.PushHandler;
import сhat.server.Server;
import сhat.user.User;
import сhat.user.type.Type;

import javax.security.auth.callback.CallbackHandler;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static messages.requesttype.RequestType.*;
import static messages.responsetype.ResponseType.*;

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
    private  PushHandler ph;

    public Client() throws IOException {
        socket = new Socket(serverHost, Server.getPort());
        try {
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Input failed");
            e.printStackTrace();
            throw e;
        }
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
        } catch (IOException e) {
            System.out.println("Output failed");
            e.printStackTrace();
            throw e;
        }

        this.ph = new PushHandler(input);
    }

    // todo: it's a STUB, need to be changed
    public void send(String msg) throws IOException {
        output.write(msg.getBytes(StandardCharsets.UTF_8));
    }

    private Response getTypedResponse(ResponseType... types) throws IOException, ClassNotFoundException {
        ph.getInput();
        Response response = null;
        try {
            boolean typeOk = false;
            while (!typeOk) {
                final var resp = (Response) input.readObject();
                typeOk = Arrays.stream(types).parallel().anyMatch(x -> x.equals(resp.type));
                if (!typeOk) {
                    PushHandler.HandleResponse(resp);
                } else {
                    response = resp;
                }
            }
        } catch (Exception e) {
            ph.releaseInput();
            throw e;
        }
        ph.releaseInput();
        return response;
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
            output.flush();
        } catch (IOException e) {
            // todo handle
            System.out.println("can't handle an object output stream");
        }

        try {
            Response resp = getTypedResponse(BOOLEAN_RESPONSE);
            return resp.ok;
        } catch (IOException e) {
            // todo handle
            System.out.println("can't handle an object output stream");
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
            output.flush();
        } catch (IOException e) {
            // todo handle
            System.out.println("can't handle an object output stream");
        }

        try {
            Response resp = getTypedResponse(INCOMING_MESSAGE);
            return resp.message.sender;
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
        try {
            output.writeObject(req);
            output.flush();
        } catch (IOException e) {
            // todo handle
            System.out.println("can't handle an object output stream");
        }

        try {
            Response resp = getTypedResponse(BOOLEAN_RESPONSE);
            return resp.ok;
        } catch (IOException e) {
            // todo handle
            System.out.println("can't handle an object output stream");
        } catch (ClassNotFoundException e) {
            // todo handle
            System.out.println("deserialization error");
        }

        return false;
    }

    public String[] getChatList(User user) {
        Request req = new Request(LIST_CHATS);
        req.message = new Message(
                "",
                Encryption.NONE,
                user,
                null,
                null
        );
        try {
            output.writeObject(req);
            output.flush();
        } catch (IOException e) {
            // todo handle
            System.out.println("can't handle an object output stream");
        }

        try {
            Response resp = getTypedResponse(CHAT_LIST);
            return resp.chatList;
        } catch (IOException e) {
            // todo handle
            System.out.println("can't handle an object output stream");
        } catch (ClassNotFoundException e) {
            // todo handle
            System.out.println("deserialization error");
        }

        return new String[]{};
    }

    public User[] getUserList() {
        // todo implement
        return null;
    }

    public void setServerHost(String host) {
        serverHost = host;
    }
}
