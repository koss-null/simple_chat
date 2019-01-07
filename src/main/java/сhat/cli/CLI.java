package сhat.cli;

import сhat.cli.holder.Holder;
import сhat.database.Database;
import сhat.cli.holder.command.Command;

import java.util.Scanner;

public class CLI {
    public static Boolean isServer;

    private static final String HELP = "::help";
    private static final String EXIT = "::exit";

    private final Database userDB;
    private final Database mailDB;

    public CLI(Database userDB, Database mailDB) {
        this.userDB = userDB;
        this.mailDB = mailDB;
    }

    // there is a strange fitch here:
    // if an action contains autoNext there is no way to use keywords
    public void start() {
        boolean exit = false;
        Command[] currentPossibleCmd = Holder.commands;

        Scanner input = new Scanner(System.in);
        while (!exit) {
            var cmd = input.next();
            // checking keywords
            switch (cmd) {
                case HELP: //":help"
                    for (var command: currentPossibleCmd) {
                        System.out.println(command.getCmd() + "\t" + command.getDescription());
                    }
                    break;
                case EXIT:
                    exit = true;
                    break;
            }
            if (exit) {break;}

            for (var command: currentPossibleCmd) {
                if (command.getCmd().equals(cmd)) {
                    var res = command.acquire();
                    if (!res.equals("")) {
                        System.out.println(res);
                        // todo: think about how it should chose the next action
                        // (possibly it should be solved using ringed links in holder code)
                    }

                    while (command.autoNext != null) {
                        command = command.autoNext;
                        res = command.acquire();
                        if (!res.equals("")) {
                            System.out.println(res);
                            // todo: think about how it should chose the next action
                            // (possibly it should be solved using ringed links in holder code)
                        }
                    }

                    if (command.next != null) {
                        currentPossibleCmd = command.next;
                    }

                    break;
                }
            }
        }
    }
}
