package org.ranch.ballshack.command.commands;

import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandManager;
import org.ranch.ballshack.setting.SettingSaver;

public class PrefixCommand extends Command {
	public PrefixCommand() {
		super("prefix", "Sets the command prefix", "prefix <prefix>");
	}

	@Override
	public void onCall(int argc, String[] argv) {
		if (argv.length <= 1) {
			log("Please provide a prefix", true);
			return;
		} else if (argv[1].length() > 1) {
			log("Prefix can only be 1 character long", true);
			return;
		}

		log("Setting prefix to: " + argv[1], true);

		CommandManager.prefix = argv[1];
		SettingSaver.SCHEDULE_SAVE.set(true);
	}
}
