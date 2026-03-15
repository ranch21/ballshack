package org.ranch.ballshack.setting.module.settings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.ranch.ballshack.util.PlayerUtil;

import java.util.Comparator;

public class SortModeSetting extends ModeSetting<SortModeSetting.SortMode> {

	public SortModeSetting(String name) {
		super(name, SortMode.DISTANCE, SortMode.values());
	}

	public Comparator<Entity> getComparator() {
		MinecraftClient mc = MinecraftClient.getInstance();

		SortMode mode = getValue();

		return switch (mode) {
			case DISTANCE -> Comparator.comparingDouble(mc.player::distanceTo);
			case FOV -> Comparator.comparingDouble(PlayerUtil::getAngleDiff);
			case HEALTH -> {
				Comparator<Entity> c = Comparator.comparingDouble(e -> (e instanceof LivingEntity le) ? le.getHealth() : 0);
				yield c.reversed();
			}
		};
	}

	public static enum SortMode {
		DISTANCE, HEALTH, FOV
	}
}
