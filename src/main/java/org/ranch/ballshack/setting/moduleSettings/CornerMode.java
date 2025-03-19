package org.ranch.ballshack.setting.moduleSettings;

import net.minecraft.client.MinecraftClient;
import org.ranch.ballshack.module.ModulePosition;

import java.util.Arrays;

public class CornerMode extends SettingMode {

	public CornerMode(String label) {
		super(0, label, Arrays.asList("TL", "TR", "BL", "BR"));
	}

	public ModulePosition getPosition() {
		MinecraftClient mc = MinecraftClient.getInstance();

		int mode = getValue();

		return ModulePosition.values()[mode];
	}
}
