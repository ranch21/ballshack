package org.ranch.ballshack.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class RotationUtil {

	private static final MinecraftClient mc = MinecraftClient.getInstance();

	public static Rotation slowlyTurnTowards(Rotation end, float maxChange)
	{
		float startYaw = mc.player.prevYaw;
		float startPitch = mc.player.prevPitch;
		float endYaw = end.yaw;
		float endPitch = end.pitch;

		float yawChange = Math.abs(MathHelper.wrapDegrees(endYaw - startYaw));
		float pitchChange =
				Math.abs(MathHelper.wrapDegrees(endPitch - startPitch));

		float maxChangeYaw = pitchChange == 0 ? maxChange
				: Math.min(maxChange, maxChange * yawChange / pitchChange);
		float maxChangePitch = yawChange == 0 ? maxChange
				: Math.min(maxChange, maxChange * pitchChange / yawChange);

		float nextYaw = limitAngleChange(startYaw, endYaw, maxChangeYaw);
		float nextPitch =
				limitAngleChange(startPitch, endPitch, maxChangePitch);

		return new Rotation(nextYaw, nextPitch);
	}

	public static float limitAngleChange(float current, float intended, float maxChange)
	{
		float currentWrapped = MathHelper.wrapDegrees(current);
		float intendedWrapped = MathHelper.wrapDegrees(intended);

		float change = MathHelper.wrapDegrees(intendedWrapped - currentWrapped);
		change = MathHelper.clamp(change, -maxChange, maxChange);

		return current + change;
	}
}
