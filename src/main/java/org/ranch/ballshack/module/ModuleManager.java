package org.ranch.ballshack.module;

import org.ranch.ballshack.module.modules.*;

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
			new Jesus()
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
