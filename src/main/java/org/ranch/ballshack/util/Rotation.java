package org.ranch.ballshack.util;

import net.minecraft.util.math.Vec3d;

public class Rotation {

	public float yaw;
	public float pitch;

	public Rotation(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Rotation(Vec3d vec3d) {
		this.pitch = (float) Math.asin(-vec3d.y);
		this.yaw = (float) Math.atan2(vec3d.x, vec3d.z);
	}
}
