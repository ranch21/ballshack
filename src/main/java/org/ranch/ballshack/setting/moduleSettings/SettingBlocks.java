package org.ranch.ballshack.setting.moduleSettings;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.ranch.ballshack.setting.ModuleSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.ranch.ballshack.BallsHack.mc;

// todo make gui
public class SettingBlocks extends ModuleSetting<List<Identifier>, SettingBlocks> {

	public SettingBlocks(String name, List<Identifier> value) {
		super(name, value);
	}

	@Override
	public int render(int mouseX, int mouseY) {
		return 0;
	}

	@Override
	public String getFormattedValue() {
		return getValue().toString();
	}

	@Override
	public JsonObject getJson() {
		JsonObject obj = new JsonObject();
		JsonArray array = new JsonArray();
		for (Identifier id : getValue()) {
			array.add(id.toString());
		}
		obj.add("ids", array);
		return obj;
	}

	@Override
	public void readJson(JsonObject jsonObject) {
		JsonArray array = jsonObject.get("ids").getAsJsonArray();
		List<Identifier> val = new ArrayList<>();
		for (JsonElement element : array) {
			val.add(Identifier.of(element.getAsString()));
		}
		setValue(val);
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
