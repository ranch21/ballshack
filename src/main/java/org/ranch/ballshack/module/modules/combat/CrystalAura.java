package org.ranch.ballshack.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.ExplosionImpl;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class CrystalAura extends Module {

	public CrystalAura() {
		super("CrystalAura", ModuleCategory.COMBAT, 0, "boomboomboom");
	}

	private Hand getHand() {
		ItemStack mainHand = mc.player.getStackInHand(Hand.MAIN_HAND);
		ItemStack offHand = mc.player.getStackInHand(Hand.OFF_HAND);

		if (mainHand.getItem() instanceof EndCrystalItem) {
			return Hand.MAIN_HAND;
		} else if (offHand.getItem() instanceof EndCrystalItem) {
			return Hand.OFF_HAND;
		}
		return null;
	}

	private float calculateExplosionDamage(Vec3d explosionPos, Entity entity, float power) {
		float amount = ExplosionImpl.calculateReceivedDamage(explosionPos, entity);
		float f = power * 2.0F;
		double d = Math.sqrt(entity.squaredDistanceTo(explosionPos)) / f;
		double e = (1.0 - d) * amount;
		return (float)((e * e + e) / 2.0 * 7.0 * f + 1.0);
	}
}