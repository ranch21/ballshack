package org.ranch.ballshack.command.commands;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;
import org.ranch.ballshack.command.suggestors.SettingSuggestor;
import org.ranch.ballshack.setting.Setting;
import org.ranch.ballshack.setting.ModuleSettingSaver;
import org.ranch.ballshack.setting.SettingsManager;

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
								.suggests(new SettingSuggestor())
								.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("value", StringArgumentType.string())
										.executes(context -> {
											String settingName = StringArgumentType.getString(context, "setting");
											String settingValue = StringArgumentType.getString(context, "value");
											Setting<?> setting = SettingsManager.getSettings().get(settingName);
											if (setting == null) return 0;

											if (setting.getValue() instanceof String) {
												((Setting<String>) setting).setValue(settingValue);
											} else if (setting.getValue() instanceof Boolean) {
												((Setting<Boolean>) setting).setValue(Boolean.valueOf(settingValue));
											} else if (setting.getValue() instanceof Number) {
												((Setting<Number>) setting).setValue(Double.valueOf(settingValue));
											} else {
												log(CMD(": ").append(Text.literal("Could not parse value").formatted(Formatting.GRAY)));
											}

											return 1;
										}))))
				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("list")
						.executes(context -> {
							JsonObject json = SettingsManager.getJson();
							log(CMD(": "));
							for (Map.Entry<String, Setting<?>> entry : SettingsManager.getSettings().entrySet()) {
								String valRaw = json.get(entry.getKey()).toString();
								String val = valRaw.length() > 20 ? valRaw.substring(0, 20) + "..." : valRaw;
								log(Text.literal(entry.getKey() + ": ").append(Text.literal(val).formatted(Formatting.GRAY)));
							}
							return 1;
						}));
	}
}
