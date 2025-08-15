package org.ranch.ballshack.module;

import org.ranch.ballshack.module.modules.client.HudEditor;
import org.ranch.ballshack.module.modules.client.Rainbow;
import org.ranch.ballshack.module.modules.client.WindowGui;
import org.ranch.ballshack.module.modules.combat.*;
import org.ranch.ballshack.module.modules.fun.BouncyGround;
import org.ranch.ballshack.module.modules.fun.TestModule;
import org.ranch.ballshack.module.modules.hud.ArmorHud;
import org.ranch.ballshack.module.modules.hud.Coordinates;
import org.ranch.ballshack.module.modules.hud.ModuleList;
import org.ranch.ballshack.module.modules.movement.*;
import org.ranch.ballshack.module.modules.player.AntiHunger;
import org.ranch.ballshack.module.modules.player.NoFall;
import org.ranch.ballshack.module.modules.player.Reach;
import org.ranch.ballshack.module.modules.render.*;
import org.ranch.ballshack.module.modules.client.ClickGui;

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
			new Reach(),
			new Flight(),
			new BouncyGround(),
			new KillAura(),
			new Jesus(),
			new AimAssist(),
			new TriggerBot(),
			new AutoTotem(),
			new ModuleList(),
			new TpAura(),
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
			new WindowGui(),
			new SafeWalk(),
			new Sprint(),
			new InfiniteElytraGlide(),
			new HudEditor(),
			new Coordinates(),
			new ArmorHud(),
			new Debug()
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

	public static void handleKeyPress(int key) {
		for (Module module : modules) {
			if (module.getBind() == key) {
				module.toggle();
			}
		}
	}
}
