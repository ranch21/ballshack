package org.ranch.ballshack.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface CameraInvoker {

	@Invoker("setPos")
	void ballshack$invokeSetPos(Vec3d pos);

	@Invoker("setRotation")
	void ballshack$invokeSetRotation(float yaw, float pitch);
}
