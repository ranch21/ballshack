package org.ranch.ballshack.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.Entity;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.util.EntityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityRenderer.class, PlayerEntityRenderer.class, LivingEntityRenderer.class, MobEntityRenderer.class})
public class EntityRendererMixin<T extends Entity> {

	@Inject(method = "hasLabel*", at = @At(value = "HEAD"), cancellable = true)
	protected void hasLabel(T entity, double squaredDistanceToCamera, CallbackInfoReturnable<Boolean> cir) {
		if (ModuleManager.getModuleByName("nametags") != null) {
			/*Nametags module = (Nametags) ModuleManager.getModuleByName("nametags");
			if (module.isEnabled() && module.targets.selected(EntityUtil.getEntityType(entity))) {
				cir.setReturnValue(false);
			}*/
		}
	}
}
