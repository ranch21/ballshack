package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.item.ItemStack;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;

public class ClearInvCommand extends Command {

	public ClearInvCommand() {
		super("ci", "Clears your inventory", CommandType.CREATIVE);
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		return builder
				.executes(context -> {
					for (int i = 0; i < mc.player.playerScreenHandler.getStacks().size(); i++) {
						mc.interactionManager.clickCreativeStack(ItemStack.EMPTY, i);
					}
					return 1;
				});
	}
}
