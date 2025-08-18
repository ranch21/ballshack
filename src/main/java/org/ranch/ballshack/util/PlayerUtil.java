package org.ranch.ballshack.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtil {

	private static final MinecraftClient mc = MinecraftClient.getInstance();

	public static void facePos(Vec3d p) {
		facePos(p.x, p.y, p.z);
	}

	public static void facePos(double x, double y, double z) {
		Rotation rot = getPosRotation(mc.player, x, y, z);

		mc.player.setYaw(mc.player.getYaw() + MathHelper.wrapDegrees(rot.yaw - mc.player.getYaw()));
		mc.player.setPitch(mc.player.getPitch() + MathHelper.wrapDegrees(rot.pitch - mc.player.getPitch()));
	}

	public static void facePosPacket(Vec3d p) {
		facePosPacket(p.x, p.y, p.z);
	}

	public static void facePosPacket(double x, double y, double z) {
		Rotation rot = getPosRotation(mc.player, x, y, z);

		if (!mc.player.hasVehicle()) {
			mc.player.headYaw = mc.player.getYaw() + MathHelper.wrapDegrees(rot.yaw - mc.player.getYaw());
			mc.player.bodyYaw = mc.player.headYaw;
			mc.player.renderPitch = mc.player.getPitch() + MathHelper.wrapDegrees(rot.pitch - mc.player.getPitch());
		}

		mc.player.networkHandler.sendPacket(
				new PlayerMoveC2SPacket.LookAndOnGround(
						mc.player.getYaw() + MathHelper.wrapDegrees(rot.yaw - mc.player.getYaw()),
						mc.player.getPitch() + MathHelper.wrapDegrees(rot.pitch - mc.player.getPitch()), mc.player.isOnGround(), mc.player.horizontalCollision));
	}

	public static Vec3d getMovementVector(double horizontalSpeed, double verticalSpeed) {
		double xVel = 0;
		double yVel = 0;
		double zVel = 0;

		if (mc.options.jumpKey.isPressed()) {
			yVel = verticalSpeed;
		} else if (mc.options.sneakKey.isPressed()) {
			yVel = -verticalSpeed;
		}

		float yawRad = mc.player.getYaw() * MathHelper.RADIANS_PER_DEGREE;

		float sideways = mc.player.input.movementSideways;
		float forwards = mc.player.input.movementForward;

		if (!(sideways == 0 && forwards == 0)) {
			float moveAngle = (float) Math.atan2(sideways, forwards);

			xVel = MathHelper.sin(-yawRad + moveAngle) * horizontalSpeed;
			zVel = MathHelper.cos(-yawRad + moveAngle) * horizontalSpeed;
		}

		return new Vec3d(xVel, yVel, zVel);
	}

	public static Rotation getPosRotation(Entity entity, Vec3d p) {
		return getPosRotation(entity, p.x, p.y, p.z);
	}

	public static Rotation getPosRotation(Entity entity, double x, double y, double z) {
		double diffX = x - entity.getX();
		double diffY = y - entity.getEyeY();
		double diffZ = z - entity.getZ();

		double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

		return new Rotation((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90f, (float) -Math.toDegrees(Math.atan2(diffY, diffXZ)));
	}

	public static float getAngleDiff(Entity entity) {
		Vec3d center = entity.getBoundingBox().getCenter();

		double diffX = center.x - mc.player.getX();
		double diffY = center.y - mc.player.getEyeY();
		double diffZ = center.z - mc.player.getZ();

		double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

		float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
		float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

		return Math.abs(MathHelper.wrapDegrees(yaw - mc.player.getYaw())) + Math.abs(MathHelper.wrapDegrees(pitch - mc.player.getPitch()));
	}

	public static double getSpeed() {
		double speed = 0.2873;
		if (mc.player.hasStatusEffect(StatusEffects.SPEED)) {
			int amplifier = mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier();
			speed *= 1.0 + 0.2 * (amplifier + 1);
		}
		if (mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
			int amplifier = mc.player.getStatusEffect(StatusEffects.SLOWNESS).getAmplifier();
			speed /= 1.0 + 0.2 * (amplifier + 1);
		}
		return speed;
	}

	public static boolean isMoving() {
		return mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0;
	}

	public static void setMovement(Vec3d movement) {
		if (movement.y > 0) {
			mc.player.input.playerInput.jump();
		}

		clearMovement();

		double yawRads = -Math.toRadians(mc.player.getYaw());

		Vec2f desiredDir = new Vec2f((float) movement.z, (float) movement.x);

		double forwardDot = desiredDir.x * Math.cos(yawRads) + desiredDir.y * Math.sin(yawRads);
		double backwardDot = -forwardDot;
		double rightDot = desiredDir.x * Math.sin(yawRads) - desiredDir.y * Math.cos(yawRads);
		double leftDot = -rightDot;

		List<MoveDirection> activeDirections = new ArrayList<>();

		double maxDot = Math.max(Math.max(forwardDot, backwardDot), Math.max(rightDot, leftDot));

		if (forwardDot >= 0.7 * maxDot) activeDirections.add(MoveDirection.FORWARD);
		if (backwardDot >= 0.7 * maxDot) activeDirections.add(MoveDirection.BACKWARD);
		if (rightDot >= 0.7 * maxDot) activeDirections.add(MoveDirection.RIGHT); //thanks gpt
		if (leftDot >= 0.7 * maxDot) activeDirections.add(MoveDirection.LEFT);

		// Apply movement without conflicting directions
		for (MoveDirection dir : activeDirections) {
			switch (dir) {
				case FORWARD -> mc.options.forwardKey.setPressed(true);
				case BACKWARD -> mc.options.backKey.setPressed(true);
				case RIGHT -> mc.options.rightKey.setPressed(true);
				case LEFT -> mc.options.leftKey.setPressed(true);
			}
		}
		//mc.player.input.playerInput. = (float) rMovement.x;
		//mc.player.input.movementForward = (float) rMovement.z;
	}

	public static void clearMovement() {
		mc.options.rightKey.setPressed(false);
		mc.options.leftKey.setPressed(false);
		mc.options.forwardKey.setPressed(false);
		mc.options.backKey.setPressed(false);
	}

	public enum MoveDirection {
		FORWARD, BACKWARD, LEFT, RIGHT
	}
}
