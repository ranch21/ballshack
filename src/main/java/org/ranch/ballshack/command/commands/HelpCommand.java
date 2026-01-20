package org.ranch.ballshack.command.commands;

import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandManager;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "Lists commands or gives command usage / help... i mean you just used it so you should know", "help | help <command>");
    }

    @Override
    public void onCall(int argc, String[] argv) {
        if (argc > 1) {
            Command c = CommandManager.getCommandByName(argv[1]);
            if (c == null) {
                log("No such command", true);
            } else {
                log(c.getName() + ": " + c.desc, false);
                log(c.usage, false);
            }

        } else {
            log("commands: ", true);
            for (Command command : CommandManager.getCommands()) {
                log(command.getName(), false);
            }
        }
    }
}
