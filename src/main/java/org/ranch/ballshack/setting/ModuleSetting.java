package org.ranch.ballshack.setting;

import net.minecraft.client.gui.DrawContext;

public abstract class ModuleSetting<T> {

	private String name;

	private T value;

	public ModuleSetting(String name, T value) {
		this.name = name;
		this.value = value;
	}

	public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}
}
