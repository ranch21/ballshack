package org.ranch.ballshack.module.modules.movement;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.GameMode;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventBlockShape;
import org.ranch.ballshack.event.events.EventPlayerInput;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.event.events.EventWalkOnFluid;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.module.settings.ModeSetting;

public class Jesus extends Module {

	public static enum Mode {
		Solid, Strider, Legit, Jitter
	}

	public final ModeSetting<Mode> mode = dGroup.add(new ModeSetting<>("Mode", Mode.Solid, Mode.values()));

	public Jesus() {
		super("Jesus", ModuleCategory.MOVEMENT, 0, "Atheists cannot explain");
	}

	@EventSubscribe
	public void onBlockShape(EventBlockShape event) {
		if (mode.getValue() == Mode.Solid && !mc.world.getFluidState(event.pos).isEmpty() && !mc.player.isSneaking() && !mc.player.isInFluid()) {
			double target = mc.world.getFluidState(event.pos).getHeight();
			if (mc.player.getY() >= target + event.pos.toBottomCenterPos().getY())
				event.shape = VoxelShapes.cuboid(0, 0, 0, 1, target, 1);
		}
	}

	@EventSubscribe
	public void onWalkOnFluid(EventWalkOnFluid event) {
		if (mode.getValue() == Mode.Strider && !mc.player.isSneaking() && isTopFluid() && mc.player.getY() >= mc.player.getBlockPos().toBottomCenterPos().getY() + 0.5f) {
			event.can = true;
		}
	}

	@EventSubscribe
	public void onPlayerInput(EventPlayerInput event) {
		if (mode.getValue() == Mode.Legit && !mc.player.isSneaking() && mc.player.isInFluid() && !mc.player.isJumping() && mc.interactionManager.getCurrentGameMode() != GameMode.CREATIVE) {
			double yVel = mc.player.getVelocity().getY();
			double target = mc.player.getBlockPos().toBottomCenterPos().getY() + 0.5f;
			if (mc.player.getY() < target || yVel < 0 || !isTopFluid()) {
				event.jump = true;
			} else {
				event.jump = false;
			}
		}
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mode.getValue() == Mode.Jitter && !mc.player.isSneaking() && mc.player.isInFluid() && !mc.player.isJumping()) {
			double target = getFluidHeight() - 0.1f;
			Vec3d vel = mc.player.getVelocity();
			if (!isTopFluid()) {
				mc.player.setVelocity(vel.getX(), 0.2, vel.getZ());
			} else if (mc.player.getY() < target) {
				mc.player.setVelocity(vel.getX(), 0.05, vel.getZ());
			} else {
				mc.player.setVelocity(vel.getX(), -0.05, vel.getZ());
			}
		}
	}

	public double getFluidHeight() {
		return mc.player.getBlockPos().toBottomCenterPos().getY() + mc.world.getFluidState(mc.player.getBlockPos()).getHeight();
	}

	public boolean isTopFluid() {
		return mc.world.getFluidState(mc.player.getBlockPos().add(0, 1, 0)).isEmpty();
	}
}
