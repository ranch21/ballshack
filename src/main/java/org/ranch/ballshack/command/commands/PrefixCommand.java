package org.ranch.ballshack.command.commands;

import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandManager;
import org.ranch.ballshack.setting.SettingSaver;

public class PrefixCommand extends Command {
	public PrefixCommand() {
		super("prefix");
	}

	@Override
	public void onCall(String[] args) {
		if (args.length <= 1) {
			log("Please provide a prefix", true);
			return;
		} else if (args[1].length() > 1) {
			log("Prefix can only be 1 character long", true);
			return;
		}

		log("Setting prefix to: " + args[1], true);

		CommandManager.prefix = args[1];
		SettingSaver.SCHEDULE_SAVE.set(true);
	}
}
