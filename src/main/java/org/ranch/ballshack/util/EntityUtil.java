package org.ranch.ballshack.util;

import com.google.common.collect.Streams;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.setting.settings.TargetsDropDown;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityUtil {

	private static MinecraftClient mc = MinecraftClient.getInstance();

	public static Vec3d getCenter(Entity e) {
		Vec3d p = e.getPos();
		return new Vec3d(p.x, p.y + e.getHeight() / 2, p.z);
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

	public static boolean isAnimal(Entity e) {
		return e instanceof PassiveEntity
				|| e instanceof AmbientEntity
				|| e instanceof WaterCreatureEntity
				|| e instanceof IronGolemEntity
				|| e instanceof SnowGolemEntity;
	}

	public static boolean filterByType(Entity e, TargetsDropDown t) {
		return (EntityUtil.isPlayer(e) && t.getPlayers())
				|| (EntityUtil.isMob(e) && t.getMobs())
				|| (EntityUtil.isAnimal(e) && t.getPassive());
	}

	public static List<Entity> getEntities(double distance, TargetsDropDown targetsDropDown, Comparator<Entity> comparator) {
		Stream<Entity> targets;

		targets = Streams.stream(mc.world.getEntities());

		return targets.filter(
						e -> EntityUtil.isAttackable(e)
								&& mc.player.canSee(e)
								&& mc.player.distanceTo(e) <= distance).filter(e -> filterByType(e, targetsDropDown))
				.sorted(comparator)
				.collect(Collectors.toList());
	}
}
