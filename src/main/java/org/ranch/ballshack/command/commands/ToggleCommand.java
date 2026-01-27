package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.suggestors.ModuleSuggestor;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleManager;

public class ToggleCommand extends Command {

	public ToggleCommand() {
		super("toggle", "Toggles a module");
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {

		return builder
				.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("module", StringArgumentType.word())
						.suggests(new ModuleSuggestor())
						.executes(ctx -> {

							String moduleName = StringArgumentType.getString(ctx, "module");

							Module mod = ModuleManager.getModuleByName(moduleName);

							if (mod == null) {
								log(CMD(": ").append(Text.literal("Unknown module").formatted(Formatting.GRAY)));
								return 0;
							}

							mod.toggle();

							log(CMD(": ").append(Text.literal(mod.isEnabled() ? "Enabled" : "Disabled" + " " + moduleName).formatted(Formatting.GRAY)));

							return 1;
						})
				);
	}
}
