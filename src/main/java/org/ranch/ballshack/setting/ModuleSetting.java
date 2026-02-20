package org.ranch.ballshack.setting;

import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.ranch.ballshack.gui.GuiUtil;
import org.ranch.ballshack.gui.clickgui.ModuleSettingRenderer;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;
import java.util.function.Supplier;

public abstract class ModuleSetting<T, SELF extends ModuleSetting<T, SELF>> implements ISetting<T> {

	private final String name;
	private String tooltip;
	private Supplier<Boolean> dependencyCondition;
	private boolean featured;

	protected T value;

	public ModuleSetting(String name, T value) {
		this.name = name;
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	protected SELF self() {
		return (SELF) this;
	}

	public SELF featured() {
		featured = true;
		return self();
	}

	public SELF tooltip(String tooltip) {
		this.tooltip = tooltip;
		return self();
	}

	public SELF depends(Supplier<Boolean> condition) {
		this.dependencyCondition = condition;
		return self();
	}

	public boolean dependencyMet() {
		if (dependencyCondition == null)
			return true;
		return dependencyCondition.get();
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
		SettingSaver.SCHEDULE_SAVE.set(true);
	}

	public String getName() {
		return name;
	}

	public String getTooltip() {
		return tooltip;
	}

	public boolean isFeatured() {
		return featured;
	}

	public abstract Widget getWidget(int x, int y, int width, int height);

	public abstract String getFormattedValue();

	public abstract JsonObject getJson();

	public abstract void readJson(JsonObject jsonObject);
}
