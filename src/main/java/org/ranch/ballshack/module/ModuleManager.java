package org.ranch.ballshack.module;

import org.ranch.ballshack.module.modules.combat.*;
import org.ranch.ballshack.module.modules.fun.BouncyGround;
import org.ranch.ballshack.module.modules.fun.TestModule;
import org.ranch.ballshack.module.modules.movement.Boatfly;
import org.ranch.ballshack.module.modules.movement.Flight;
import org.ranch.ballshack.module.modules.movement.Jesus;
import org.ranch.ballshack.module.modules.movement.Speed;
import org.ranch.ballshack.module.modules.player.NoFall;
import org.ranch.ballshack.module.modules.player.Reach;
import org.ranch.ballshack.module.modules.render.ClickGui;
import org.ranch.ballshack.module.modules.render.ESP;
import org.ranch.ballshack.module.modules.render.ModuleList;
import org.ranch.ballshack.module.modules.render.Tracers;

import java.util.Arrays;
import java.util.List;

public class ModuleManager {

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
			new Speed()
	);

	public static List<Module> getModules() {
		return modules;
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
