package org.ranch.ballshack.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventSetSneaking;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.util.PlayerSim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

	@Inject(method = "getCustomName", at = @At("HEAD"), cancellable = true)
	public void getCustomName(CallbackInfoReturnable<Text> cir) {
		if (ModuleManager.getModuleByName("nametags") != null && ModuleManager.getModuleByName("nametags").isEnabled()) {
			cir.setReturnValue(null);
		}
	}

	@Inject(method = "hasCustomName", at = @At("HEAD"), cancellable = true)
	public void hasCustomName(CallbackInfoReturnable<Boolean> cir) {
		if (ModuleManager.getModuleByName("nametags") != null && ModuleManager.getModuleByName("nametags").isEnabled()) {
			cir.setReturnValue(false);
		}
	}
}
