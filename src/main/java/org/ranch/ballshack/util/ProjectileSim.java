package org.ranch.ballshack.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.*;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProjectileSim {

	private static final MinecraftClient mc = MinecraftClient.getInstance();

	public static ProjectileEntity prepareSim(PlayerEntity player) {

		ItemStack mHand = player.getStackInHand(Hand.MAIN_HAND);
		ItemStack oHand = player.getStackInHand(Hand.OFF_HAND);

		ItemStack stack;

		if (InvUtil.isThrowable(mHand.getItem())) {
			stack = mHand;
		} else if (InvUtil.isThrowable(oHand.getItem())) {
			stack = oHand;
		} else return null;

		if (stack.getItem() instanceof RangedWeaponItem) {

			float charged = stack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(stack) ? 1f
					: stack.getItem() == Items.CROSSBOW ? 0f : BowItem.getPullProgress(player.getItemUseTime());

			if (charged > 0f) {
				ProjectileEntity e = new ArrowEntity(EntityType.ARROW, mc.world);
				initProjectile(e, player, 0f, charged * 3);
				return e;
			}
		} else if (stack.getItem() instanceof ExperienceBottleItem) {

			ProjectileEntity e = new ExperienceBottleEntity(EntityType.EXPERIENCE_BOTTLE, mc.world);
			initProjectile(e, player, -20f, 0.7f);
			return e;

		} else if (stack.getItem() instanceof ThrowablePotionItem) {

			ProjectileEntity e = new SplashPotionEntity(EntityType.SPLASH_POTION, mc.world);
			initProjectile(e, player, -20f, 0.5f);
			return e;

		} else if (stack.getItem() instanceof TridentItem) {

			ProjectileEntity e = new TridentEntity(EntityType.TRIDENT, mc.world);
			initProjectile(e, player, 0f, 2.5f);
			return e;

		} else if (stack.getItem() instanceof ProjectileItem) {

			ProjectileEntity e = new SnowballEntity(EntityType.SNOWBALL, mc.world);
			initProjectile(e, player, 0f, 1.5f);
			return e;

		}

		return null;
	}

	private static void initProjectile(ProjectileEntity e, Entity thrower, float addPitch, float strength) {
		e.setPos(thrower.getX(), thrower.getEyeY(), thrower.getZ());
		e.setVelocity(thrower, thrower.getPitch() + addPitch, thrower.getYaw(), 0.0f, strength, 0);
	}

	public static Trajectory simulate(ProjectileEntity e, boolean fake, @Nullable Entity thrower) {
		List<Vec3d> traj = new ArrayList<>();

		Projectile proj = new Projectile(e);
		for (int i = 0; i < 100; i++) {

			Vec3d vel = proj.velocity;

			Vec3d newPos = proj.pos.add(vel);

			List<LivingEntity> entities = mc.world.getEntitiesByClass(LivingEntity.class, proj.getBoundingBox().expand(0.15),
					EntityPredicates.VALID_LIVING_ENTITY.and(en -> en != mc.player && en != e));

			if (!entities.isEmpty()) {
				return new Trajectory(traj, entities.get(0), null, fake, thrower, proj);
			}

			BlockHitResult blockHit = mc.world.raycast(
					new RaycastContext(proj.pos, newPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, e));

			if (blockHit.getType() != HitResult.Type.MISS) {
				traj.add(blockHit.getPos());
				return new Trajectory(traj, null, blockHit.getBlockPos(), fake, thrower, proj);
			}

			float prevPitch = proj.pitch;
			proj.pitch = MathHelper.lerp(0.2F, proj.prevPitch, proj.pitch);
			proj.prevPitch = prevPitch;

			proj.applyDrag();

			double grav = getGravity(e);

			proj.velocity = proj.velocity.add(0, -grav, 0);

			proj.pos = new Vec3d(proj.pos.x + proj.velocity.x, proj.pos.y + proj.velocity.y, proj.pos.z + proj.velocity.z);

			traj.add(proj.pos);
		}

		return new Trajectory(traj, null, null, fake, thrower, proj);
	}

	public static double getGravity(ProjectileEntity e) {
		if (e instanceof PersistentProjectileEntity) {
			return 0.05;
		} else if (e instanceof PotionEntity) {
			return 0.05;
		} else if (e instanceof ExperienceBottleEntity) {
			return 0.07;
		} else if (e instanceof LlamaSpitEntity) {
			return 0.06;
		} else if (e instanceof ThrownEntity) {
			return 0.03;
		} else {
			return 0;
		}
	}

	public static class Trajectory {
		private final List<Vec3d> positions;
		private final Entity entity;
		private final Projectile projectile;
		private final Entity thrower;
		private final BlockPos pos;
		private final boolean fake;

		public Trajectory(List<Vec3d> positions, @Nullable Entity entity, @Nullable BlockPos pos, boolean isFake, @Nullable Entity thrower, Projectile projectile) {
			this.positions = positions;
			this.entity = entity;
			this.pos = pos;
			this.fake = isFake;
			this.thrower = thrower;
			this.projectile = projectile;
		}

		public List<Vec3d> getPositions() {
			return positions;
		}

		public Entity getEntity() {
			return entity;
		}

		public BlockPos getPos() {
			return pos;
		}

		public boolean isFake() {
			return fake;
		}

		public Entity getThrower() {
			return thrower;
		}

		public Projectile getProjectile() {
			return projectile;
		}
	}

	public static class Projectile {
		public Vec3d velocity;
		public Vec3d pos;

		public float yaw;
		public float pitch;

		public float prevYaw;
		public float prevPitch;

		public float width;
		public float height;

		public Entity orig;

		public final float DRAG = 0.99f;

		public Projectile(Entity e) {

			velocity = e.getVelocity();
			pos = e.getEntityPos();

			this.yaw = e.getYaw();
			this.pitch = e.getPitch();

			prevYaw = yaw;
			prevPitch = pitch;

			width = e.getWidth();
			height = e.getHeight();
		}

		public Box getBoundingBox() {
			return new Box(pos.x, pos.y, pos.z, pos.x + width, pos.y + height, pos.z + width);
		}

		public void applyDrag() {
			this.velocity = new Vec3d(velocity.x * DRAG, velocity.y * DRAG, velocity.z * DRAG);
		}
	}
}
