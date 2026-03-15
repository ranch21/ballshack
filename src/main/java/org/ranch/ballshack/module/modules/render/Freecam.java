package org.ranch.ballshack.module.modules.render;

import net.minecraft.block.ShapeContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.*;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.module.settings.BooleanSetting;
import org.ranch.ballshack.setting.module.settings.NumberSetting;
import org.ranch.ballshack.util.PlayerUtil;
import org.ranch.ballshack.util.Rotation;

/*todo
	make 2 modes: Entity and Camera,
	camera: what is already done
	entity: gives player noclip and fly and stops sending packets
*/
public class Freecam extends Module {

	public Vec3d prevPos = Vec3d.ZERO;
	public Vec3d pos = Vec3d.ZERO;
	public Rotation rotation = new Rotation(0, 0);

	public NumberSetting speed = dGroup.add(new NumberSetting("Speed", 1).min(0.25).max(5).step(0.25));

	public final BooleanSetting rotate = dGroup.add(new BooleanSetting("Look at crosshair", false));

	public Freecam() {
		super("Freecam", ModuleCategory.RENDER, 0);
	}

	@EventSubscribe
	public void onCameraUpdate(EventCameraUpdate event) {
		event.pos = prevPos.lerp(pos, event.tickProgress);
	}

	@EventSubscribe
	public void onCameraRotate(EventCameraRot event) {
		event.rot = rotation;
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		prevPos = pos;
		pos = pos.add(PlayerUtil.getMovementVector(speed.getValue(), speed.getValue()));

		Vec3d raycastEnd = new Vec3d(0, 0, 100)
				.rotateX(-(float) Math.toRadians(rotation.pitch))
				.rotateY(-(float) Math.toRadians(rotation.yaw))
				.add(pos);

		if (rotate.getValue()) {
			BlockHitResult hit = mc.world.raycast(new RaycastContext(
					pos, raycastEnd,
					RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, ShapeContext.absent()
			));

			if (hit.getType() != HitResult.Type.MISS) {
				PlayerUtil.facePos(hit.getPos());
			}
		}
	}

	@EventSubscribe
	public void onMouseMove(EventMouseUpdate.Entity event) {
		rotation = new Rotation(
				(float) (mc.gameRenderer.getCamera().getYaw() + event.origDeltaX * 0.15F),
				(float) (mc.gameRenderer.getCamera().getPitch() + event.origDeltaY * 0.15F)
		);
		rotation.pitch = MathHelper.clamp(rotation.pitch, -90.0F, 90.0F);
		event.cancel();
	}

	@EventSubscribe
	public void onIsThirdPerson(EventRenderSelf event) {
		event.render = true;
	}

	@EventSubscribe
	public void onPlayerInput(EventPlayerInput event) {
		event.cancel();
	}

	@EventSubscribe
	public void onPlayerMovementVector(EventPlayerMovementVector event) {
		event.cancel();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		mc.chunkCullingEnabled = true;
	}

	@Override
	public void onEnable() {
		if (mc.world == null) {
			super.onDisable();
		} else {
			super.onEnable();
			mc.chunkCullingEnabled = false;
			pos = mc.player.getEyePos();
			rotation = new Rotation(mc.player.getYaw(), mc.player.getPitch());
		}
	}
}
