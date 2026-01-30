package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;
import org.ranch.ballshack.gui.ThemeManager;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.module.modules.client.Themes;

public class ThemeCommand extends Command {
	public ThemeCommand() {
		super("theme", "Loads or unloads a theme", CommandType.CLIENT);
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		return builder
				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("load")
						.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("theme", StringArgumentType.string())
								.executes(context -> {
									String theme = StringArgumentType.getString(context, "theme");
									ThemeManager.loadTheme(theme);
									((Themes) ModuleManager.getModuleByName("themes")).theme.setValue(theme);
									return 1;
								})))

				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("unload")
								.executes(context -> {
									ThemeManager.clearTheme();
									return 1;
								}));
	}
}
