package сhat.cli;

import сhat.cli.holder.Holder;
import сhat.database.Database;
import сhat.cli.holder.command.Command;

import java.util.Scanner;

public class CLI {
    public static Boolean isServer;

    private static final String HELP = "::help";
    private static final String EXIT = "::exit";

    private static final String MAN = "ResponseType:\n"
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
        Holder.init(userDB, mailDB);
        Command[] currentPossibleCmds = Holder.commands;

        Scanner input = new Scanner(System.in);
        while (!exit) {
            var cmd = input.next();
            boolean help = false;
            // checking keywords
            switch (cmd) {
                case HELP: //":help"
                    for (var command: currentPossibleCmds) {
                        System.out.println(command.getCmd() + ":\t:" + command.getDescription());
                    }
                    help = true;
                    break;
                case EXIT:
                    exit = true;
                    break;
            }
            if (help) {continue;}
            if (exit) {break;}

            var wasAcquired = false;
            for (var command: currentPossibleCmds) {
                if (command.getCmd().equals(cmd)) {
                    wasAcquired = true;

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

            if (!wasAcquired) {
                System.out.println("There is no such command, requesttype " + HELP + " to see the command list");
            }
        }
    }
}
