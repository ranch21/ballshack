package org.ranch.ballshack.mixin;

import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.Packet;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventPacketSend;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonNetworkHandler.class)
public class ClientCommonNetworkHandlerMixin {

	@Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
	public void sendPacket(Packet<?> packet, CallbackInfo ci) {
		EventPacketSend event = new EventPacketSend(packet);
		BallsHack.eventBus.post(event);
		if (event.isCancelled()) {
			ci.cancel();
		}
	}
}
