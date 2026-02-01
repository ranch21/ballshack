package org.ranch.ballshack.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.Resources;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.events.EventPlayerMovementVector;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

		float sideways = mc.player.input.getMovementInput().x;
		float forwards = mc.player.input.getMovementInput().y;

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

	public static void setMovement(Vec3d movement, EventPlayerMovementVector event) {
		if (movement.y > 0) {
			mc.player.input.playerInput.jump();
		}

		double yawRads = Math.toRadians(mc.player.getYaw());

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
		event.movement = new Vec2f(0, 0);
		for (MoveDirection dir : activeDirections) {
			switch (dir) {
				case FORWARD -> event.movement = event.movement.add(new Vec2f(0, 1));
				case BACKWARD -> event.movement = event.movement.add(new Vec2f(0, -1));
				case RIGHT -> event.movement = event.movement.add(new Vec2f(1, 0));
				case LEFT -> event.movement = event.movement.add(new Vec2f(-1, 0));
			}
		}
	}

	public enum MoveDirection {
		FORWARD, BACKWARD, LEFT, RIGHT
	}

	public static String getCape(UUID uuid) {
		if (uuid.equals(UUID.fromString("b21498e5-f45b-4c64-a860-a64941ca4e44"))) {
			return "jeffito";
		} else if (uuid.equals(UUID.fromString("39590f72-cdfb-4bd8-b32e-69d82a020ae6"))) {
			return "walinchi";
		} else if (uuid.equals(UUID.fromString("eefc1e48-bc40-44ec-a7c0-25e89b66e543"))) {
			return "valentina";
		}

		return null;
	}


	public static CompletableFuture<GameProfile> fetchProfile(String username) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				JsonObject playerData = JsonParser.parseString(
						Resources.toString(new URL("https://api.mojang.com/users/profiles/minecraft/" + username), StandardCharsets.UTF_8)
				).getAsJsonObject();

				String uuidStr = playerData.get("id").getAsString().replaceFirst(
						"(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w+)",
						"$1-$2-$3-$4-$5"
				);

				UUID uuid = UUID.fromString(uuidStr);

				return new GameProfile(uuid, playerData.get("name").getAsString());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	public static CompletableFuture<GameProfile> fetchProperties(GameProfile profile) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				JsonObject playerData = JsonParser.parseString(
						Resources.toString(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + profile.id().toString().replace("-", "")), StandardCharsets.UTF_8)
				).getAsJsonObject();

				JsonArray properties = playerData.getAsJsonArray("properties");

				Multimap<String, Property> mutable = HashMultimap.create(profile.properties());

				for (JsonElement element : properties) {
					JsonObject property = element.getAsJsonObject();
					mutable.put(
							property.get("name").getAsString(),
							new Property(property.get("name").getAsString(), property.get("value").getAsString())
					);
				}

				return new GameProfile(profile.id(), profile.name(), new PropertyMap(mutable));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}
}
