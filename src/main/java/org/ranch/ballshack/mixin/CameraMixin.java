package org.ranch.ballshack.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventCameraExtend;
import org.ranch.ballshack.event.events.EventCameraRot;
import org.ranch.ballshack.event.events.EventCameraUpdate;
import org.ranch.ballshack.util.FreelookHandler;
import org.ranch.ballshack.util.Rotation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public class CameraMixin {

	@ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
	public void update(Args args) {
		float yaw = args.get(0);
		float pitch = args.get(1);
		if (FreelookHandler.getEnabled()) {
			yaw = FreelookHandler.yaw;
			pitch = FreelookHandler.pitch;
		}
		EventCameraRot event = new EventCameraRot(new Rotation(yaw, pitch));
		BallsHack.eventBus.post(event);
		args.set(0, event.rot.yaw);
		args.set(1, event.rot.pitch);
	}

	@ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
	public void setPos(Args args, @Local(argsOnly = true) float tickProgress) {
		EventCameraUpdate event = new EventCameraUpdate(new Vec3d(args.get(0), args.get(1), args.get(2)), tickProgress);
		BallsHack.eventBus.post(event);
		args.set(0, event.pos.x);
		args.set(1, event.pos.y);
		args.set(2, event.pos.z);
	}

	@ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;moveBy(FFF)V", ordinal = 0))
	public void moveBy(Args args) {
		EventCameraExtend event = new EventCameraExtend(args.get(0), args.get(1), args.get(2));
		BallsHack.eventBus.post(event);
		args.set(0, event.f);
		args.set(1, event.g);
		args.set(2, event.h);
	}
}
