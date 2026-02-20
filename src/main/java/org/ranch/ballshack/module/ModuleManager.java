package org.ranch.ballshack.module;

import net.minecraft.client.input.KeyInput;
import org.ranch.ballshack.module.modules.client.*;
import org.ranch.ballshack.module.modules.fun.TestModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

	public static final boolean printToggle = false;

	static final List<Module> modules = new ArrayList<>();

	private static void register(Module module) {
		modules.add(module);
	}

	public static List<Module> getModules() {
		return modules;
	}

	public static List<Module> getModulesByCategory(ModuleCategory category) {
		List<Module> m = new ArrayList<>();
		for (Module module : modules) {
			if (module.getCategory() == category) {
				m.add(module);
			}
		}
		return m;
	}

	public static Module getModuleByName(String name) {
		for (Module module : modules) {
			if (module.getName().equalsIgnoreCase(name)) {
				return module;
			}
		}
		return null;
	}

	public static void handleKeyPress(KeyInput key) {
		for (Module module : modules) {
			if (module.getBind() == key.key()) {
				module.toggle();
			}
		}
	}

	static {
		// client
		register(new ClickGui());
		register(new NekoModule());

		// combat

		// fun
		register(new TestModule());

		// hud

		// movement

		// player

		// render

		// world
	}
}
