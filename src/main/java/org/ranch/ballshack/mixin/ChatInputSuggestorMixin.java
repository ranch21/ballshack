package org.ranch.ballshack.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.ranch.ballshack.command.CommandManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatInputSuggestor.class)
public class ChatInputSuggestorMixin {

	@Final
	@Shadow
	TextFieldWidget textField;

	@Redirect(method = "refresh", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;getCommandDispatcher()Lcom/mojang/brigadier/CommandDispatcher;"))
	public CommandDispatcher<ClientCommandSource> getCommandDispatcher(ClientPlayNetworkHandler instance) {
		if (textField.getText().charAt(0) == CommandManager.prefix) {
			return CommandManager.getCommandDispatcher();
		}
		return instance.getCommandDispatcher();
	}

	@Redirect(method = "refresh", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;peek()C"))
	public char peek(StringReader instance) {
		if (instance.peek() == CommandManager.prefix) return '/'; // WORSE THAN EPSTEIN?
		return instance.peek();
	}
}
