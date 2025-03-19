package org.ranch.ballshack.module.modules.movement;

import net.minecraft.util.shape.VoxelShapes;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventBlockShape;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;

import java.util.Arrays;

public class Jesus extends Module {
	public Jesus() {
		super("Jesus", ModuleCategory.MOVEMENT, GLFW.GLFW_KEY_J, new ModuleSettings(Arrays.asList(
				new SettingToggle(true, "Sides").featured()
		)), "Atheists cannot explain");
	}

	@EventSubscribe
	public void onBlockShape(EventBlockShape event) {
		if (!mc.world.getFluidState(event.pos).isEmpty() // event is water
				&& !mc.player.isSneaking() // not sneaking
				&& !mc.player.isTouchingWater() // not touching water
		) {
			if ((boolean) getSettings().getSetting(0).getValue()) {
				event.shape = VoxelShapes.cuboid(0, 0, 0, 1, 0.9, 1);
			} else if(mc.player.getY() >= event.pos.getY() + 0.9) { // above water
				event.shape = VoxelShapes.cuboid(0, 0, 0, 1, 0.9, 1);
			}
		}
	}
}
