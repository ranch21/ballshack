package org.ranch.ballshack.command.commands;

import net.minecraft.client.util.InputUtil;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleManager;

public class BindCommand extends Command {
	public BindCommand() {
		super("bind", "Binds a module", "bind set <module> <key> | bind clear <module>");
	}

	@Override
	public void onCall(int argc, String[] argv) {
		if (argc < 3)
			log("Invalid action", true);

		String action = argv[1];
		Module mod = ModuleManager.getModuleByName(argv[2]);

		if (action.equals("set") && argc > 3) {
			int key = -1;
			try {
				key = InputUtil.fromTranslationKey("key.keyboard." + argv[3].toLowerCase().charAt(0)).getCode();
			} catch (IllegalArgumentException exception) {
				log("wgat key si that", true);
			}

			if (key != -1) {
				mod.getSettings().getBind().setValue(key);
				log("Bound", true);
			}
		} else if (action.equals("clear")) {
			mod.getSettings().getBind().setValue(0);
			log("Cleared", true);
		} else {
			log("Invalid action", true);
		}
	}
}
