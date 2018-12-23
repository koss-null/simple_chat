package Chat.CLI.Holder.Command;

import java.util.function.Supplier;

public class Command {
    private final String cmd;
    private final String description;
    private final Supplier<String> action;
    public final Command[] next;
    public final Command autoNext;

    public Command(final String cmd, final String description, Supplier<String> action) {
        this.cmd = cmd;
        this.description = description;
        this.action = action;
        this.next = null;
        this.autoNext = null;
    }

    public Command(final String cmd, final String description, Supplier<String> action, Command[] next) {
        this.cmd = cmd;
        this.description = description;
        this.action = action;
        this.next = next;
        this.autoNext = null;
    }

    public Command(final String cmd, final String description, Supplier<String> action, Command autoNext) {
        this.cmd = cmd;
        this.description = description;
        this.action = action;
        this.next = null;
        this.autoNext = autoNext;
    }

    public static boolean needCall() {
        return true;
    }

    public String acquire() {
        return action.get();
    }

    public String getCmd() {
        return this.cmd;
    }

    public String getDescription() {
        return this.description;
    }
}