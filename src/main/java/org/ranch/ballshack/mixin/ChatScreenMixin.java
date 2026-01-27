package org.ranch.ballshack.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

	@Shadow
	protected TextFieldWidget chatField;

	@Inject(method = "sendMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addToMessageHistory(Ljava/lang/String;)V", ordinal = 0), cancellable = true)
	public void sendMessage(String chatText, boolean addToHistory, CallbackInfo ci) {
		if (!chatText.isEmpty() && chatText.charAt(0) == CommandManager.prefix.getValue()) {
			if (addToHistory) {
				BallsHack.mc.inGameHud.getChatHud().addToMessageHistory(chatText);
			}

			CommandManager.executeCommand(chatText.substring(1), null);
			ci.cancel();
		}
	}

	@Redirect(method = "sendMessage", at = @At(value = "INVOKE", target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z"))
	public boolean startsWith(String instance, String prefix) {
		if (instance.charAt(0) == CommandManager.prefix.getValue()) {
			return true;
		}
		return instance.startsWith(prefix);
	}

}
