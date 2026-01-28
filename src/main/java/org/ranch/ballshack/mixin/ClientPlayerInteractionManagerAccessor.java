package org.ranch.ballshack.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerInteractionManager.class)
public interface ClientPlayerInteractionManagerAccessor {

	@Mutable
	@Accessor("blockBreakingCooldown")
	abstract void setBlockBreakingCooldown(int cooldown);

	@Accessor("blockBreakingCooldown")
	abstract int getBlockBreakingCooldown();
}
