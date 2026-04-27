package org.ranch.ballshack.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.ranch.ballshack.command.Command;
import org.ranch.ballshack.command.CommandType;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.module.modules.render.Search;
import org.ranch.ballshack.module.modules.render.Waypoints;
import org.ranch.ballshack.setting.module.ModuleSettingSaver;

import java.awt.*;
import java.util.Random;

public class WaypointCommand extends Command {
	public WaypointCommand() {
		super("waypoints", "modify waypoints", CommandType.SURVIVAL);
	}

	@Override
	public LiteralArgumentBuilder<ClientCommandSource> onRegister(LiteralArgumentBuilder<ClientCommandSource> builder) {
		Random random = new Random();
		return builder
				.then(LiteralArgumentBuilder.<ClientCommandSource>literal("add")
						.then(RequiredArgumentBuilder.<ClientCommandSource, String>argument("name", StringArgumentType.string())
							.executes(context -> {
								Waypoints waypoints = ModuleManager.getModuleByClass(Waypoints.class);
								String name = StringArgumentType.getString(context, "name");
								BlockPos pos = mc.player.getBlockPos();
								Color col = Color.getHSBColor(random.nextFloat(), 0.7f, 1.0f);
								waypoints.list.getValue().add(new Waypoints.Waypoint(name, col, pos));
								return 0;
							})));
	}
}
