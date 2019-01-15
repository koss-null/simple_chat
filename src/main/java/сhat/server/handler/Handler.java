package сhat.server.handler;

import messages.message.Message;
import messages.message.encryption.Encryption;
import messages.request.Request;
import messages.response.Response;
import messages.responsetype.ResponseType;
import сhat.database.Database;
import сhat.user.User;
import сhat.user.type.Type;
import сhat.utils.pair.Pair;

import java.io.*;
import java.net.Socket;

public class Handler extends Thread {
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

    private boolean checkLogin(String login) {
        var user = userDb.getKeys(new String[]{"users", login});
        return user != null;
    }

    private boolean loginRegister(String login, String pass) {
        if (!checkLogin(login)) {
            userDb.set(new String[]{"users", login}, new User(login, pass, Type.REGULAR));
            return true;
        }
        return false;
    }

    // returns pair with the user and the status of pass check (false if isn't equals)
    private Pair<User, Boolean> registerCheck(String login, String pass) {
        var user = (User[]) userDb.getKeys(new String[]{"users", login});
        if (user == null || user.length == 0) {
            return new Pair<>(null, true);
        }
        for (var u : user) {
            if (u.name.equals(login)) {
                if (u.pass.equals(pass)) {
                    return new Pair<>(u, true);
                } else {
                    return new Pair<>(null, false);
                }
            }
        }
        return new Pair<>(null, true);
    }

    @Override
    public void run() {
        System.out.println("Started a new handler, waiting a client message");

        boolean done = false;
        while (!done) {
            try (ObjectInputStream in = new ObjectInputStream(input)) {
                var req = (Request) in.readObject();
                switch (req.type) {
                    case LOGIN_CHECK: {
                        var exist = checkLogin(req.message.sender.name);
                        Response resp = new Response(ResponseType.BOOLEAN_RESPONSE, exist);

                        try (ObjectOutputStream out = new ObjectOutputStream(output)) {
                            out.writeObject(resp);
                        } catch (IOException e) {
                            // todo handle
                            System.out.println("can't create an object output stream");
                        }
                        break;
                    }
                    case LOGIN_REGISTER: {
                        var ok = loginRegister(req.message.sender.name, req.message.sender.pass);
                        Response resp = new Response(ResponseType.BOOLEAN_RESPONSE, ok);

                        try (ObjectOutputStream out = new ObjectOutputStream(output)) {
                            out.writeObject(resp);
                        } catch (IOException e) {
                            // todo handle
                            System.out.println("can't create an object output stream");
                        }
                        break;
                    }
                    case REGISTER_CHECK: {
                        Pair<User, Boolean> user = registerCheck(req.message.sender.name, req.message.sender.pass);
                        Response resp;
                        if (user.Value()) {
                            resp = new Response(
                                    ResponseType.INCOMING_MESSAGE,
                                    new Message("", Encryption.NONE, user.Key(), null, null)
                            );
                        } else {
                            resp = new Response(
                                    ResponseType.SERVICE_MESSAGE,
                                    "Wrong password"
                            );
                        }

                        try (ObjectOutputStream out = new ObjectOutputStream(output)) {
                            out.writeObject(resp);
                        } catch (IOException e) {
                            // todo handle
                            System.out.println("can't create an object output stream");
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Some troubles with the server, please restart " +
                        "or contact with the product owner to fix the problem");
                e.printStackTrace();
                done = true;
            } catch (ClassNotFoundException e) {
                System.out.println("Some troubles with message deserialize");
            }

        }
    }
}