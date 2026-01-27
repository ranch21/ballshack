package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;
import org.ranch.ballshack.command.arguments.CharacterArgumentType;
import org.ranch.ballshack.command.suggestors.ModuleSuggestor;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleManager;

public class BindCommand extends Command {

	public BindCommand() {
		super("bind", "Binds a module", CommandType.CLIENT);
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {

		return builder

				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("set")
						.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("module", StringArgumentType.word())
								.suggests(new ModuleSuggestor())
								.then(RequiredArgumentBuilder.<ClientCommandSource, Character>argument("key", new CharacterArgumentType())
										.executes(context -> {

											String moduleName = StringArgumentType.getString(context, "module");
											Character keyChar = CharacterArgumentType.getCharacter(context, "key");

											Module mod = ModuleManager.getModuleByName(moduleName);

											if (mod == null) {
												log(CMD(": ").append(Text.literal("Unknown module").formatted(Formatting.GRAY)));
												return 0;
											}

											int key = -1;
											try {
												key = InputUtil.fromTranslationKey(
														"key.keyboard." + keyChar
												).getCode();
											} catch (Exception ignored) {
											}

											if (key == -1) {
												log(CMD(": ").append(Text.literal("Invalid key").formatted(Formatting.GRAY)));
												return 0;
											}

											mod.getSettings().getBind().setValue(key);
											log(CMD(": ").append(Text.literal("Bound " + moduleName + " to " + keyChar).formatted(Formatting.GRAY)));

											return 1;
										})
								)
						)
				)

				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("clear")
						.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("module", StringArgumentType.word())
								.executes(context -> {

									String moduleName = StringArgumentType.getString(context, "module");
									Module mod = ModuleManager.getModuleByName(moduleName);

									if (mod == null) {
										log(CMD(": ").append(Text.literal("Unknown module").formatted(Formatting.GRAY)));
										return 0;
									}

									mod.getSettings().getBind().setValue(0);
									log(CMD(": ").append(Text.literal("Cleared bind for " + moduleName).formatted(Formatting.GRAY)));

									return 1;
								})
						)
				);
	}
}
