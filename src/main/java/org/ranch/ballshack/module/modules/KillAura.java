package org.ranch.ballshack.module.modules;

import com.google.common.collect.Streams;
import net.minecraft.entity.Entity;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.DropDown;
import org.ranch.ballshack.setting.settings.SettingSlider;
import org.ranch.ballshack.setting.settings.SettingToggle;
import org.ranch.ballshack.util.EntityUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KillAura extends Module {

	public KillAura() {
		super("KillAura", ModuleCategory.COMBAT, GLFW.GLFW_KEY_R, new ModuleSettings(Arrays.asList(
				new SettingSlider(4, "Range", 1, 8, 0.5),
				new DropDown("Multi", Arrays.asList(
						new SettingToggle(true, "Enabled"),
						new SettingSlider(4,"Targets",2,10, 1)
				)),
				new DropDown("Targets", Arrays.asList(
						new SettingToggle(true, "Players"),
						new SettingToggle(true, "Monsters"),
						new SettingToggle(false, "Passive")
				))
		)));
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.player.getAttackCooldownProgress(mc.getTickDelta()) == 1.0f) {

			int attacked = 0;

			for (Entity e : getEntities()) {
				mc.interactionManager.attackEntity(mc.player, e);
				attacked++;

				DropDown dropDown = (DropDown) getSettings().getSetting(1);

				if (attacked >= (int) (double) dropDown.getSetting(1).getValue() && (boolean) dropDown.getSetting(0).getValue()) {
					break;
				}

				if (!(boolean) dropDown.getSetting(0).getValue()) {
					break;
				}
			}
		}


	}

	private List<Entity> getEntities() {
		Stream<Entity> targets;

		targets = Streams.stream(mc.world.getEntities());

		double dist = (double) getSettings().getSetting(0).getValue();

		Comparator<Entity> comparator = Comparator.comparing(mc.player::distanceTo);

		DropDown dropDown = (DropDown) getSettings().getSetting(2);

		boolean players = (boolean) dropDown.getSetting(0).getValue();
		boolean monsters = (boolean) dropDown.getSetting(1).getValue();
		boolean animals = (boolean) dropDown.getSetting(2).getValue();

		return targets.filter(
				e -> EntityUtil.isAttackable(e)
				&& mc.player.canSee(e)
				&& mc.player.distanceTo(e) <= dist)
				.filter(e ->
						(EntityUtil.isPlayer(e) && players)
						|| (EntityUtil.isMob(e) && monsters)
						|| (EntityUtil.isAnimal(e) && animals))
				.sorted(comparator)
				.collect(Collectors.toList());
	}
}
