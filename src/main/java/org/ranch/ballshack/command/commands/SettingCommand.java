package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;
import org.ranch.ballshack.command.suggestors.SettingSuggester;
import org.ranch.ballshack.setting.client.ClientSetting;
import org.ranch.ballshack.setting.client.ClientSettingSaver;

import java.util.Map;

public class SettingCommand extends Command {

	public SettingCommand() {
		super("settings", "configure and list settings (debug).", CommandType.CLIENT);
	}

	@SuppressWarnings("unchecked")
	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		return builder
				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("set")
						.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("setting", StringArgumentType.string())
								.suggests(new SettingSuggester())
								.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("value", StringArgumentType.string())
										.executes(context -> {
											String settingName = StringArgumentType.getString(context, "setting");
											String settingValue = StringArgumentType.getString(context, "value");
											ClientSetting<?> setting = ClientSettingSaver.getSettings().get(settingName);
											if (setting == null) return 0;

											if (setting.getValue() instanceof String) {
												((ClientSetting<String>) setting).setValue(settingValue);
											} else if (setting.getValue() instanceof Boolean) {
												((ClientSetting<Boolean>) setting).setValue(Boolean.valueOf(settingValue));
											} else if (setting.getValue() instanceof Number) {
												((ClientSetting<Number>) setting).setValue(Double.valueOf(settingValue));
											} else {
												log(CMD(": ").append(Text.literal("Could not parse value").formatted(Formatting.GRAY)));
											}

											return 1;
										}))))
				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("list")
						.executes(context -> {
							log(CMD(": "));
							for (Map.Entry<String, ClientSetting<?>> entry : ClientSettingSaver.getSettings().entrySet()) {
								String valRaw = entry.getValue().getFormattedValue();
								MutableText val = Text.literal(valRaw.length() > 40 ? valRaw.substring(0, 40) + "..." : valRaw)
										.formatted(Formatting.GRAY);
								log(Text.literal(entry.getKey() + ": ").append(val));
							}
							return 1;
						}));
	}
}
