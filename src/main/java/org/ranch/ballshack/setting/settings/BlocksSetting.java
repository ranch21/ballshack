package org.ranch.ballshack.setting.settings;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
	public JsonElement getJson() {
		JsonArray array = new JsonArray();
		getValue().forEach((block) -> {
			array.add(block.toString());
		});
		return array;
	}

	@Override
	public void readJson(JsonElement jsonElement) {
		List<Identifier> ids = new  ArrayList<>();
		jsonElement.getAsJsonArray().forEach((element) -> {
			ids.add(Identifier.tryParse(element.getAsString()));
		});
		setValue(ids);
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
