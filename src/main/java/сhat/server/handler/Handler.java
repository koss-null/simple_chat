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
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    private final Database userDb;
    private final Database mailDb;

    public Handler(Socket socket, Database userDb, Database mailDb) throws IOException {
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.output.flush();
        this.input = new ObjectInputStream(new DataInputStream(socket.getInputStream()));

        this.userDb = userDb;
        this.mailDb = mailDb;
    }

    private boolean checkLogin(String login) {
        var user = userDb.get(new String[]{"users", login});
        return user != null;
    }

    private boolean loginRegister(String login, String pass) {
        if (!checkLogin(login)) {
            userDb.set(new String[]{"users", login}, new User(login, pass, Type.REGULAR));
            userDb.store();
            return true;
        }
        return false;
    }

    // returns pair with the user and the status of pass check (false if isn't equals)
    private Pair<User, Boolean> registerCheck(String login, String pass) {
        var user = (User) userDb.get(new String[]{"users", login});
        if (user == null) {
            return new Pair<>(null, false);
        }

        if (user.name.equals(login) && user.pass.equals(pass)) {
            return new Pair<>(user, true);
        } else {
            return new Pair<>(null, true);
        }
    }


    @Override
    public void run() {
        System.out.println("Started a new handler, waiting a client message");

        boolean done = false;
        while (!done) {
            try {
                var req = (Request) input.readObject();
                switch (req.type) {
                    case LOGIN_CHECK: {
                        var exist = checkLogin(req.message.sender.name);
                        Response resp = new Response(ResponseType.BOOLEAN_RESPONSE, exist);

                        try {
                            output.writeObject(resp);
                            output.flush();
                        } catch (IOException e) {
                            // todo handle
                            System.out.println("can't create an object output stream");
                        }
                        break;
                    }
                    case LOGIN_REGISTER: {
                        var ok = loginRegister(req.message.sender.name, req.message.sender.pass);
                        Response resp = new Response(ResponseType.BOOLEAN_RESPONSE, ok);

                        try {
                            output.writeObject(resp);
                            output.flush();
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

                        try {
                            output.writeObject(resp);
                            output.flush();
                        } catch (IOException e) {
                            // todo handle
                            System.out.println("can't handle an object output stream");
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("It looks like one of the clients got off");
                //e.printStackTrace();
                done = true;
            } catch (ClassNotFoundException e) {
                System.out.println("Some troubles with message deserialize");
            }

        }
    }
}