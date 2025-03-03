package org.ranch.ballshack.setting.settings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.ranch.ballshack.util.PlayerUtil;

import java.util.Arrays;
import java.util.Comparator;

public class SortMode extends SettingMode {

	public SortMode(String label) {
		super(0, label, Arrays.asList("Dist", "Ang", "HP"));
	}

	public Comparator<Entity> getComparator() {
		MinecraftClient mc = MinecraftClient.getInstance();

		int mode = getValue();

		if (mode == 0) {
			return Comparator.comparing(mc.player::distanceTo);
		} else if (mode == 1) {
			return Comparator.comparing(PlayerUtil::getAngleDiff);
		} else {
			Comparator<Entity> c = Comparator.comparing(e -> e.isLiving() ? ((LivingEntity) e).getHealth() : 0);
			return c.reversed();
		}
	}
}
