package org.ranch.ballshack.module;

import net.minecraft.client.input.KeyInput;
import org.ranch.ballshack.module.modules.client.*;
import org.ranch.ballshack.module.modules.combat.*;
import org.ranch.ballshack.module.modules.fun.TestModule;
import org.ranch.ballshack.module.modules.hud.ArmorHud;
import org.ranch.ballshack.module.modules.hud.Coordinates;
import org.ranch.ballshack.module.modules.hud.ModuleList;
import org.ranch.ballshack.module.modules.hud.Watermark;
import org.ranch.ballshack.module.modules.movement.*;
import org.ranch.ballshack.module.modules.player.*;
import org.ranch.ballshack.module.modules.render.*;
import org.ranch.ballshack.module.modules.world.Scaffold;

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
		register(new ColorsConfig());
		register(new DiscordRP());
		register(new HudEditor());
		register(new Rainbow());

		// combat
		register(new AimAssist());
		register(new AutoTotem());
		register(new Criticals());
		register(new KillAura());
		register(new TriggerBot());

		// fun
		register(new TestModule());

		// hud
		register(new ArmorHud());
		register(new Coordinates());
		register(new ModuleList());
		register(new Watermark());

		// movement
		register(new Boatfly());
		register(new Flight());
		register(new InfiniteElytraGlide());
		register(new Jesus());
		//register(new ProjectileEvade());
		register(new SafeWalk());
		register(new Speed());
		register(new Sprint());

		// player
		register(new AntiHunger());
		//register(new AutoReconnect());
		register(new AutoRespawn());
		register(new AutoTool());
		register(new AutoWalk());
		register(new NoFall());

		// render
		register(new BlockHighlight());
		register(new ChestESP());
		register(new Debug());
		register(new ESP());
		register(new Tracers());
		register(new Trajectories());

		// world
		register(new Scaffold());
	}
}
