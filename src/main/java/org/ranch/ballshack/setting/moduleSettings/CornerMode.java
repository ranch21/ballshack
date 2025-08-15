package org.ranch.ballshack.setting.moduleSettings;

import net.minecraft.client.MinecraftClient;
import org.ranch.ballshack.module.ModuleAnchor;

import java.util.Arrays;

public class CornerMode extends SettingMode {

	public CornerMode(String label) {
		super(0, label, Arrays.asList("TL", "TR", "BL", "BR"));
	}

	public ModuleAnchor getPosition() {
		MinecraftClient mc = MinecraftClient.getInstance();

		int mode = getValue();

		return ModuleAnchor.values()[mode];
	}
}
