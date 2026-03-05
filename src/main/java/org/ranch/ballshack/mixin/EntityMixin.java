package org.ranch.ballshack.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventSetSneaking;
import org.ranch.ballshack.event.events.EventSlow;
import org.ranch.ballshack.util.PlayerSim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "setSneaking", at = @At("HEAD"), cancellable = true)
	private void setSneaking(boolean sneaking, CallbackInfo ci) {
		if ((Object) this instanceof PlayerEntity) {
			EventSetSneaking event = new EventSetSneaking(sneaking);
			BallsHack.eventBus.post(event);
			if (event.isCancelled()) {
				ci.cancel();
			}
		}
	}

	@Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
	public void pushAwayFrom(Entity entity, CallbackInfo ci) {
		if (entity instanceof PlayerSim.FakePlayer) {
			ci.cancel();
		}
	}

	@ModifyVariable(method = "slowMovement", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	public Vec3d slowMovement(Vec3d multiplier) {
		EventSlow event = new EventSlow(multiplier);
		BallsHack.eventBus.post(event);
		return event.mult;
	}
}
