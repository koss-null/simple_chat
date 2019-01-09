package сhat.cli;

import сhat.cli.holder.Holder;
import сhat.database.Database;
import сhat.cli.holder.command.Command;

import java.util.Scanner;

public class CLI {
    public static Boolean isServer;

    private static final String HELP = "::help";
    private static final String EXIT = "::exit";

    private static final String MAN = "Type:\n"
            + HELP + " to get available commands,\n"
            + EXIT + " to quit";

    private final Database userDB;
    private final Database mailDB;

    public CLI(Database userDB, Database mailDB) {
        this.userDB = userDB;
        this.mailDB = mailDB;
    }

    // there is a strange fitch here:
    // if an action contains autoNext there is no way to use keywords
    public void start() {
        System.out.println(MAN);

        boolean exit = false;
        Command[] currentPossibleCmds = Holder.commands;

        Scanner input = new Scanner(System.in);
        while (!exit) {
            var cmd = input.next();
            // checking keywords
            switch (cmd) {
                case HELP: //":help"
                    for (var command: currentPossibleCmds) {
                        System.out.println(command.getCmd() + ":\t:" + command.getDescription());
                    }
                    break;
                case EXIT:
                    exit = true;
                    break;
            }
            if (exit) {break;}

            for (var command: currentPossibleCmds) {
                if (command.getCmd().equals(cmd)) {
                    var res = command.acquire();
                    if (!res.equals("")) {
                        System.out.println(res);
                    }

                    while (command.autoNext != null) {
                        command = command.autoNext;
                        res = command.acquire();
                        if (!res.equals("")) {
                            System.out.println(res);
                        }
                    }

                    // here it's possible to have some ringed links
                    // to make an infinite state machine of actions
                    if (command.next != null) {
                        currentPossibleCmds = command.next;
                    }

                    break;
                }
            }
        }
    }
}
