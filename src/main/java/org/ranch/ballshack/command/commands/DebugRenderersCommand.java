package org.ranch.ballshack.command.commands;

import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.debug.DebugRenderers;

import java.util.List;

public class DebugRenderersCommand extends Command {
	public DebugRenderersCommand() {
		super("drenderers", "enables / disables debug renderers", "drenderers (enable|disable) <renderer> | drenderers list");
	}

	@Override
	public void onCall(int argc, String[] argv) {
		if (argv.length <= 1) {
			log("Please provide an action (enable, disable, list)", true);
			return;
		}
        switch (argv[1]) {
            case "enable":
                if (DebugRenderers.setEnabled(argv[2], true)) {
                    log("Enabled renderer: " + argv[2], true);
                }
                break;
            case "disable":
                if (DebugRenderers.setEnabled(argv[2], false)) {
                    log("Disabled renderer: " + argv[2], true);
                }
                break;
            case "list":
                List<String> renderers = DebugRenderers.getIdList();
                log("Renderers:", true);
                for (String id : renderers) {
                    log(id + " " + DebugRenderers.getRenderer(id).getEnabled(), false);
                }
                break;
            default:
                log("Please provide a valid action (enable, disable, list)", true);
        }
    }
}
