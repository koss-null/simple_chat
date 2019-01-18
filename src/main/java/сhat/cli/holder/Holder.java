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

    private static Command list = new Command(
            "list",
            "shows all avalible chats",
            () -> {
                Actions.getChatList(currentUser);
                return "";
            }
    );

    private static Command newChat = new Command(
            "new",
            "create new personal or group chat",
            () -> {
                Actions.newChat();
                return "";
            }
    );

    // initiates inside a constructor to get rid of ringed links
    private static Command sendDialog;

    private static Command enter = new Command(
            "enter",
            "enters chousen chat",
            () -> {
                // todo: implement
                return "";
            },
            sendDialog
    );

    private static Command chat = new Command(
            "chat",
            "a stub to start chat when user is logged in",
            () -> "",
            new Command[]{
                    list,
                    newChat,
                    enter
            }
    );

    private static Command getLoginCommand() {
        return new Command(
                "login",
                "performs login operation",
                () -> {
                    try {
                        currentUser = Actions.login(Holder.userDB);
                        if (currentUser == null) {
                            login.autoNext = login;
                            return "Wrong login or password";
                        }
                        login.autoNext = chat;
                        System.out.println("Successfully logged in");
                        return "";
                    } catch (IOException e) {
                        return "Some troubles while logging.\n" + e.toString();
                    }
                }
        );
    }

    private static Command login = getLoginCommand();

    private static Command exit = new Command(
            "exit",
            "finishes the server",
            () -> {
                System.exit(0);
                return "";
            }
    );

    private static Command server = new Command(
            "server",
            "creates server",
            () -> {
                Actions.server(userDB, messageDB);
                return "";
            },
            new Command[]{exit}
    );

    private static Command logon = new Command(
            "logon",
            "add a new user",
            () -> {
                boolean success = Actions.logOn();
                if (!success) {
                    return "Login failed";
                }
                return "";
            }
    );

    private static Command client = new Command(
            "client",
            "initiates a client creation",
            () -> {
                Actions.client();
                return "";
            },
            new Command[]{
                    login,
                    logon
            }
    );

    public static final Command[] commands = new Command[]{
            server,
            client
    };

    public static void init(Database userDB, Database messageDB) {
        Holder.userDB = userDB;
        Holder.messageDB = messageDB;

        sendDialog = new Command(
                "dialog",
                "implements chatting process",
                // todo: implement
                () -> "",
                new Command[] {
                        list,
                        newChat,
                        enter
                }
        );
    }
}
