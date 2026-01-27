package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.suggestors.DebugRendererSuggestor;
import org.ranch.ballshack.debug.DebugRenderers;

import java.util.List;

public class DebugRenderersCommand extends Command {
	public DebugRenderersCommand() {
		super("debug_renderers", "enables / disables debug renderers");
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		return builder
				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("enable")
						.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("renderer", StringArgumentType.string())
								.suggests(new DebugRendererSuggestor())
								.executes(context -> {
									String renderer = StringArgumentType.getString(context, "renderer");
									if (DebugRenderers.setEnabled(renderer, true)) {
										log(CMD(": ").append(Text.literal("Enabled renderer " + renderer).formatted(Formatting.GRAY)));
									}
									return 1;
								})))

				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("disable")
						.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("renderer", StringArgumentType.string())
								.suggests(new DebugRendererSuggestor())
								.executes(context -> {
									String renderer = StringArgumentType.getString(context, "renderer");
									if (DebugRenderers.setEnabled(renderer, false)) {
										log(CMD(": ").append(Text.literal("Disabled renderer " + renderer).formatted(Formatting.GRAY)));
									}
									return 1;
								})))

				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("list")
						.executes(context -> {
							List<String> renderers = DebugRenderers.getIdList();
							MutableText rlist = Text.literal("");
							log(CMD(": ").append(Text.literal("Renderers: ").formatted(Formatting.GRAY)));
							for (String renderer : renderers) {
								if (!rlist.getString().isEmpty()) {
									rlist.append(Text.literal(", "));
								}
								rlist.append(Text.literal(renderer).formatted(DebugRenderers.getRenderer(renderer).getEnabled() ? Formatting.GREEN : Formatting.RED));
							}
							log(rlist.formatted(Formatting.GRAY));
							return 1;
						}));
	}
}
