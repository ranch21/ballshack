package org.ranch.ballshack.debug;

import com.google.gson.reflect.TypeToken;
import org.ranch.ballshack.debug.renderers.BallGridDebugRenderer;
import org.ranch.ballshack.setting.Setting;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DebugRenderers {

	private static LinkedHashMap<String,DebugRenderer> renderers = new LinkedHashMap<>();

	public static Setting<List<Boolean>> enabled = new Setting<>(new ArrayList<>(), "debugRenderers", new TypeToken<List<Boolean>>() {
	}.getType());

	public static void registerRenderer(String id,DebugRenderer renderer) {
		renderers.put(id,renderer);
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
		for (int i = 0; i < list.size(); i++) {
			int finalI = i;
			renderers.values().forEach(renderer -> renderer.setEnabled(list.get(finalI)));
		}
	}

	static {
		registerRenderer("ballgrid", new BallGridDebugRenderer());
	}
}
