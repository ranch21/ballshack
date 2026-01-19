package org.ranch.ballshack.module;

import net.minecraft.client.input.KeyInput;

import org.ranch.ballshack.module.modules.client.*;
import org.ranch.ballshack.module.modules.combat.*;
import org.ranch.ballshack.module.modules.fun.*;
import org.ranch.ballshack.module.modules.hud.*;
import org.ranch.ballshack.module.modules.movement.*;
import org.ranch.ballshack.module.modules.player.*;
import org.ranch.ballshack.module.modules.render.*;
import org.ranch.ballshack.module.modules.world.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleManager {

	public static boolean printToggle = false;

	static List<Module> modules = Arrays.asList(
			new TestModule(),
			new ClickGui(),
			new Boatfly(),
			new NoFall(),
			new Flight(),
			new KillAura(),
			new Jesus(),
			new AimAssist(),
			new TriggerBot(),
			new AutoTotem(),
			new ModuleList(),
			new ESP(),
			new Tracers(),
			new Speed(),
			new AntiHunger(),
			new ChestESP(),
			new Rainbow(),
			new Criticals(),
			new BlockHighlight(),
			new Trajectories(),
			new ProjectileEvade(),
			new SafeWalk(),
			new Sprint(),
			new DiscordRP(),
			new InfiniteElytraGlide(),
			new HudEditor(),
			new Coordinates(),
			new ArmorHud(),
			new Debug(),
			new ColorsConfig(),
			new Watermark(),
			new Scaffold()
	);

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
			if (module.getName().equals(name)) {
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
}
