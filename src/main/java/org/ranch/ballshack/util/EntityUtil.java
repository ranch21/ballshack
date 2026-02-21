package org.ranch.ballshack.util;

import com.google.common.collect.Streams;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import org.ranch.ballshack.FriendManager;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.setting.TargetsSettingGroup;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityUtil {

	private static final PerlinNoiseSampler noise = new PerlinNoiseSampler(Random.create());
	private static final MinecraftClient mc = MinecraftClient.getInstance();

	public static Vec3d getCenter(Entity e) {
		Vec3d p = e.getEntityPos();
		return new Vec3d(p.x, p.y + e.getHeight() / 2, p.z);
	}


	public static Rotation getRotation(Entity entity) {
		return new Rotation(entity.getYaw(), entity.getPitch());
	}

	public static boolean isAttackable(Entity e) {
		return (e instanceof LivingEntity || e instanceof ShulkerBulletEntity || e instanceof AbstractFireballEntity)
				&& e.isAlive()
				&& e != MinecraftClient.getInstance().player
				&& !e.isConnectedThroughVehicle(MinecraftClient.getInstance().player);
	}

	public static boolean isMob(Entity e) {
		return e instanceof Monster;
	}

	public static boolean isPlayer(Entity e) {
		return e instanceof PlayerEntity && e != MinecraftClient.getInstance().player;
	}

	public static boolean isFriend(Entity e) {
		if (isPlayer(e)) {
			return FriendManager.has(e.getName().getString());
		}
		return false;
	}

	public static EntityType getEntityType(Entity e) {
		if (isFriend(e)) return EntityType.FRIEND;
		else if (isPlayer(e)) return EntityType.PLAYER;
		else if (isAnimal(e)) return EntityType.PASSIVE;
		else if (e instanceof Angerable) return EntityType.NEUTRAL;
		else if (isMob(e)) return EntityType.MONSTER;
		else return EntityType.OTHER;
	}

	public static boolean isAnimal(Entity e) {
		return e instanceof PassiveEntity
				|| e instanceof AmbientEntity
				|| e instanceof WaterCreatureEntity
				|| e instanceof IronGolemEntity
				|| e instanceof SnowGolemEntity;
	}

	public static Color getHPCol(LivingEntity entity) {
		return getCol(entity.getHealth() / entity.getMaxHealth());
	}

	public static Color getCol(float t) {
		float f = t * 3;

		if (f < 1)
			return Colors.DULL_RED;
		else if (f < 2)
			return Color.YELLOW;
		else
			return Colors.DULL_GREEN;
	}

	public static Vec3d getNoiseOffset(double randSpeed, double randAmount) {
		double time = mc.world.getTime() * (randSpeed / 100);
		Vec3d offset = new Vec3d(
				noise.sample(time, 0.23, 0.11),
				noise.sample(0.86, time, 0.55),
				noise.sample(0.28, 0.58, time)
		);

		return offset.multiply(randAmount);
	}

	public static int getMaxAllowedFloatingTicks(Entity entity) {
		double d = entity.getFinalGravity();
		if (d < 9.999999747378752E-6) {
			return Integer.MAX_VALUE;
		} else {
			double e = 0.08 / d;
			return MathHelper.ceil(80.0 * Math.max(e, 1.0));
		}
	}

	public static boolean filterByType(Entity e, TargetsSettingGroup t) {
		return t.selected(getEntityType(e));
	}

	public static List<Entity> getEntities(double distance, TargetsSettingGroup targetsSettingGroup, Comparator<Entity> comparator, boolean raycast) {
		Stream<Entity> targets;

		if (mc.world == null) return new ArrayList<>();

		targets = Streams.stream(mc.world.getEntities());

		return targets.filter(
						e -> EntityUtil.isAttackable(e)
								&& (mc.player.canSee(e) || raycast)
								&& mc.player.distanceTo(e) <= distance).filter(e -> filterByType(e, targetsSettingGroup))
				.sorted(comparator)
				.collect(Collectors.toList());
	}

	public enum EntityType {
		PLAYER(new Color(200, 50, 100)),
		FRIEND(Color.BLUE),
		PASSIVE(Color.GREEN),
		NEUTRAL(Color.ORANGE),
		MONSTER(Color.RED),
		OTHER(Color.GRAY);

		private final Color color;

		EntityType(Color color) {
			this.color = color;
		}

		public Color getColor() {
			return color;
		}
	}
}
