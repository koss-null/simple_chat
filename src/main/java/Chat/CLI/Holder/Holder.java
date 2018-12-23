package Chat.CLI.Holder;

import Chat.CLI.Holder.Actions.Actions;
import Chat.CLI.Holder.Actions.Actions.LoginState;
import Chat.CLI.Holder.Command.Command;
import Chat.Database.Database;

import java.io.IOException;

public class Holder {
    private static Database userDB;
    private static Database messageDB;

    public static final Command[] commands = new Command[]{
            new Command(
                    "server",
                    "creates server",
                    () -> {
                        try {
                            Actions.server();
                        } catch (IOException e) {
                            return ("Some troubles have been arisen due the server startup.\n" + e.toString());
                        }
                        return "";
                    }),

            new Command(
                    "client",
                    "initiates a client creation",
                    () -> {
                        Actions.client();
                        return "";
                    },
                    new Command(
                            "login",
                            "performs login operation",
                            () -> {
                                try {
                                    switch (Actions.login(Holder.userDB)) {
                                        case OK:
                                            return "";
                                        case FAILED:
                                            return "Wrong login or password";
                                    }
                                } catch (IOException e) {
                                    return "Some troubles while logging.\n" + e.toString();
                                }
                                return "Internal code error, please contact to a project owner(3)";
                            }
                    )
            ),
    };

    public Holder (Database userDB, Database messageDB) {
        Holder.userDB = userDB;
        Holder.messageDB = messageDB;
    }
}
