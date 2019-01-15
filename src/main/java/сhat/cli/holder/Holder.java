package сhat.cli.holder;

import сhat.cli.holder.actions.Actions;
import сhat.cli.holder.command.Command;
import сhat.database.Database;
import сhat.user.User;

import java.io.IOException;

public class Holder {
    private static Database userDB;
    private static Database messageDB;

    private static User currentUser = null;

    private static Command afterLogin = null;

    private static Command chat = new Command(
            "chat",
            "a stub to start chat when user is logged in",
            () -> "",
            new Command[]{
                    new Command(
                            "list",
                            "shows all avalible chats",
                            () -> ""
                    ),
                    new Command(
                            "new",
                            "create new personal or group chat",
                            () -> ""
                    ),
            }
    );

    // todo: check if it works as it was mentioned
    private static Command getLoginCommand() {
        return new Command(
                "login",
                "performs login operation",
                () -> {
                    try {
                        currentUser = Actions.login(Holder.userDB);
                        if (currentUser == null) {
                            afterLogin = login;
                            return "Wrong login or password";
                        }
                        afterLogin = chat;
                        return "";
                    } catch (IOException e) {
                        return "Some troubles while logging.\n" + e.toString();
                    }
                },
                afterLogin
        );
    }

    private static Command login = getLoginCommand();

    public static final Command[] commands = new Command[]{
            new Command(
                    "server",
                    "creates server",
                    () -> {
                        Actions.server(userDB, messageDB);
                        return "";
                    },
                    new Command[]{new Command(
                            "exit",
                            "finishes the server",
                            () -> {
                                System.exit(0);
                                return "";
                            }
                    )}),

            new Command(
                    "client",
                    "initiates a client creation",
                    () -> {
                        Actions.client();
                        return "";
                    },
                    new Command[]{
                            login,
                            new Command(
                                    "logon",
                                    "add a new user",
                                    () -> {
                                        boolean success = Actions.logOn();
                                        if (!success) {
                                            return "Login failed";
                                        }
                                        return "";
                                    },
                                    login
                            ),
                            // todo remove it
                            new Command(
                                    "send",
                                    "sending a message to a server",
                                    () -> {
                                        Actions.send();
                                        return "";
                                    }
                            )
                    }
            ),
    };

    public static void init(Database userDB, Database messageDB) {
        Holder.userDB = userDB;
        Holder.messageDB = messageDB;
    }
}
