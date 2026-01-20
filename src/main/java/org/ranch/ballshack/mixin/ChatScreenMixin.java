package org.ranch.ballshack.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

	@Shadow
	protected TextFieldWidget chatField;

	@Inject(method = "sendMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addToMessageHistory(Ljava/lang/String;)V", ordinal = 0), cancellable = true)
	public void sendMessage(String chatText, boolean addToHistory, CallbackInfo ci) {
		if (chatText.startsWith(CommandManager.prefix)) {
			if (addToHistory) {
				BallsHack.mc.inGameHud.getChatHud().addToMessageHistory(chatText);
			}
			CommandManager.onCommand(chatText.substring(CommandManager.prefix.length()));
			ci.cancel();
		}
	}

	@Inject(method = "keyPressed", at = @At(value = "HEAD"))
	public void keyPressed(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
		if (chatField.getText().startsWith(CommandManager.prefix) && input.isTab()) {
			if (chatField.getText().length() == CommandManager.prefix.length()) {
				chatField.setText(CommandManager.prefix + CommandManager.getCommands().get(0).getName());
			} else {
				List<String> possible = new ArrayList<>();
				for (Command command : CommandManager.getCommands()) {
					if (command.getName().startsWith(chatField.getText().substring(CommandManager.prefix.length()))) {
						possible.add(command.getName());
					}
				}
				if (!possible.isEmpty()) {
					chatField.setText(CommandManager.prefix + possible.get(0));
				}
			}
		}
	}
}
