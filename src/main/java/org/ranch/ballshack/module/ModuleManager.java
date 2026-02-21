package org.ranch.ballshack.module;

import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.module.modules.client.*;
import org.ranch.ballshack.module.modules.combat.*;
import org.ranch.ballshack.module.modules.fun.TestModule;
import org.ranch.ballshack.module.modules.hud.ArmorHud;
import org.ranch.ballshack.module.modules.hud.Coordinates;
import org.ranch.ballshack.module.modules.hud.ModuleList;
import org.ranch.ballshack.module.modules.hud.Watermark;

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

	public static <T extends Module> T getModuleByClass(Class<T> clazz) {
		for (Module module : modules) {
			if (module.getClass() == clazz) {
				return (T)module;
			}
		}
		return null;
	}

	public static void handleKeyPress(KeyInput key, int action) {
		for (Module module : modules) {
			if (module.getBind() == key.key() && action == GLFW.GLFW_PRESS) {
				module.toggle();
			} else if (module.holdSetting.getValue() && action == GLFW.GLFW_RELEASE) {
				module.toggle();
			}
		}
	}

	static {
		// client
		register(new ClickGui());
		register(new NekoModule());
		register(new DiscordRP());
		register(new HudEditor());
		register(new Rainbow());
		register(new Themes());

		// combat
		register(new AimAssist());
		register(new AutoTotem());
		register(new Criticals());
		register(new CrystalAura());
		register(new KillAura());

		// fun
		register(new TestModule());

		// hud
		register(new ArmorHud());
		register(new Coordinates());
		register(new ModuleList());
		register(new Watermark());

		// movement

		// player

		// render

		// world
	}
}
