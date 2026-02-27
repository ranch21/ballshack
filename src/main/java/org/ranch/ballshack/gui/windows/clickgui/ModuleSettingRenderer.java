package org.ranch.ballshack.gui.windows.clickgui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import org.joml.Vector2f;
import org.ranch.ballshack.setting.module.ModuleSetting;

public interface ModuleSettingRenderer<T> extends Element {
	Vector2f render(Vector2f pos, ModuleSetting<T, ?> setting, DrawContext context, double mouseX, double mouseY, float deltaTicks);
}
