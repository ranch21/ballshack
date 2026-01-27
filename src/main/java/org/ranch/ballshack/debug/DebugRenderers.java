package org.ranch.ballshack.debug;

import com.google.gson.reflect.TypeToken;
import org.ranch.ballshack.debug.renderers.BallGridDebugRenderer;
import org.ranch.ballshack.debug.renderers.PlayerSimDebugRenderer;
import org.ranch.ballshack.debug.renderers.ScaffoldDebugRenderer;
import org.ranch.ballshack.debug.renderers.VecDebugRenderer;
import org.ranch.ballshack.setting.Setting;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DebugRenderers {

	private static final LinkedHashMap<String, DebugRenderer> renderers = new LinkedHashMap<>();

	public static final Setting<List<Boolean>> enabled = new Setting<>(new ArrayList<>(), "debugRenderers", new TypeToken<List<Boolean>>() {
	}.getType());

	public static void registerRenderer(String id, DebugRenderer renderer) {
		renderers.put(id, renderer);
	}

	public static DebugRenderer getRenderer(String id) {
		return renderers.get(id);
	}

	private static List<Boolean> getBooleanList() {
		List<Boolean> list = new ArrayList<>();
		renderers.values().forEach((renderer) -> list.add(renderer.getEnabled()));
		return list;
	}

	public static List<String> getIdList() {
		return new ArrayList<>(renderers.keySet());
	}

	public static boolean setEnabled(String id, boolean enabled) {
		DebugRenderer renderer = renderers.get(id);
		if (renderer == null) return false;
		renderer.setEnabled(enabled);
		DebugRenderers.enabled.setValue(getBooleanList());
		return true;
	}

	public static void load() {
		List<Boolean> list = enabled.getValue();
		int i = 0;

		for (DebugRenderer renderer : renderers.values()) {
			if (i >= list.size()) break;
			renderer.setEnabled(list.get(i));
			i++;
		}
	}

	static {
		registerRenderer("ballgrid", new BallGridDebugRenderer());
		registerRenderer("input", new VecDebugRenderer());
		registerRenderer("velocity", new VecDebugRenderer());
		registerRenderer("scaffold", new ScaffoldDebugRenderer());
		registerRenderer("playersim", new PlayerSimDebugRenderer());
	}
}
