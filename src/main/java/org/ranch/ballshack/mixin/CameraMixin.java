package org.ranch.ballshack.mixin;

import net.minecraft.client.render.Camera;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventCameraExtend;
import org.ranch.ballshack.event.events.EventCameraRot;
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

	@ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;moveBy(FFF)V", ordinal = 0))
	public void moveBy(Args args) {
		EventCameraExtend event = new EventCameraExtend(args.get(0), args.get(1), args.get(2));
		BallsHack.eventBus.post(event);
		args.set(0, event.f);
		args.set(1, event.g);
		args.set(2, event.h);
	}
}
