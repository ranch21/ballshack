package org.ranch.ballshack.setting.settings;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.setting.ModuleSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.ranch.ballshack.BallsHack.mc;

public class BlocksSetting extends ModuleSetting<List<Identifier>, BlocksSetting> {

	public BlocksSetting(String name) {
		super(name, List.of());
	}

	public BlocksSetting(String name, List<Identifier> value) {
		super(name, value);
	}

	@Override
	public Widget getWidget(int x, int y, int width, int height) {
		return null;
	}

	@Override
	public String getFormattedValue() {
		return "";
	}

	@Override
	public JsonObject getJson() {
		return null;
	}

	@Override
	public void readJson(JsonObject jsonObject) {

	}

	private CommandRegistryAccess getRegistry() {
		return CommandRegistryAccess.of(mc.world.getRegistryManager(), mc.world.getEnabledFeatures());
	}

	public List<BlockState> getBlockStates() {
		List<BlockState> states = new ArrayList<>();
		for (Identifier id : getValue()) {
			states.add(getRegistry().getOrThrow(RegistryKeys.BLOCK).getOptional(RegistryKey.of(RegistryKeys.BLOCK, id)).orElseThrow(NoSuchElementException::new).value().getDefaultState());
			//masterful
		}
		return states;
	}

	public List<Block> getBlocks() {
		List<Block> blocks = new ArrayList<>();
		getBlockStates().forEach((blockState -> blocks.add(blockState.getBlock())));
		return blocks;
	}
}
