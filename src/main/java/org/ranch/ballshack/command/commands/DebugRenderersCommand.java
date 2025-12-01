package org.ranch.ballshack.command.commands;

import org.ranch.ballshack.FriendManager;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandManager;
import org.ranch.ballshack.debug.DebugRenderers;
import org.ranch.ballshack.setting.SettingSaver;

import java.util.List;

public class DebugRenderersCommand extends Command {
	public DebugRenderersCommand() {
		super("debug_renderers");
	}

	@Override
	public void onCall(String[] args) {
		if (args.length <= 1) {
			log("Please provide an action (enable, disable, list)", true);
			return;
		}
		if (args.length > 2) {
			switch (args[1]) {
				case "enable":
					if (DebugRenderers.setEnabled(args[2], true)) {
						log("Enabled renderer: " + args[2], true);
					}
					break;
				case "disable":
					if (DebugRenderers.setEnabled(args[2], false)) {
						log("Disabled renderer: " + args[2], true);
					}
					break;
				case "list":
					List<String> renderers = DebugRenderers.getIdList();
					log("Renderers:", true);
					for (String id : renderers) {
						log(id, false);
					}
					break;
				default:
					log("Please provide a valid action (enable, disable, list)", true);
			}
		}
	}
}
