package org.ranch.ballshack.module.modules.render;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.*;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
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
	public Vec3d vel = Vec3d.ZERO;
	public Rotation rotation = new Rotation(0, 0);

	public NumberSetting speed = dGroup.add(new NumberSetting("Speed", 0.25).min(0).max(5).step(0.25));

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
