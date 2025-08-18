package org.ranch.ballshack.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class RotationUtil {

	private static final MinecraftClient mc = MinecraftClient.getInstance();

	public static float getDegreeChange(float start, float end) {
		return Math.abs(MathHelper.wrapDegrees(end - start));
	}

	public static Rotation slowlyTurnTowards(Rotation end, float maxChange) {
		float startYaw = mc.player.prevYaw;
		float startPitch = mc.player.prevPitch;
		float endYaw = end.yaw;
		float endPitch = end.pitch;

		float yawChange = getDegreeChange(startYaw, endYaw);
		float pitchChange = getDegreeChange(startPitch, endPitch);

		float maxChangeYaw = pitchChange == 0 ? maxChange
				: Math.min(maxChange, maxChange * yawChange / pitchChange);
		float maxChangePitch = yawChange == 0 ? maxChange
				: Math.min(maxChange, maxChange * pitchChange / yawChange);

		float nextYaw = limitAngleChange(startYaw, endYaw, maxChangeYaw);
		float nextPitch =
				limitAngleChange(startPitch, endPitch, maxChangePitch);

		return new Rotation(nextYaw, nextPitch);
	}

	public static float limitAngleChange(float current, float intended, float maxChange) {
		float currentWrapped = MathHelper.wrapDegrees(current);
		float intendedWrapped = MathHelper.wrapDegrees(intended);

		float change = MathHelper.wrapDegrees(intendedWrapped - currentWrapped);
		change = MathHelper.clamp(change, -maxChange, maxChange);

		return current + change;
	}

	public static Rotation getRotation(Entity e) {
		return new Rotation(e.getYaw(), e.getPitch());
	}
}
