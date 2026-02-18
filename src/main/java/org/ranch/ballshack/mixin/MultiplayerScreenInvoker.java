package org.ranch.ballshack.mixin;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiplayerScreen.class)
public interface MultiplayerScreenInvoker {

	@Invoker("addEntry")
	void ballshack$invokeAddEntry(boolean confirmedAction);
}
