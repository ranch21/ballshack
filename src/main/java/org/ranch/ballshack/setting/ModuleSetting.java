package org.ranch.ballshack.setting;

import com.google.gson.JsonElement;
import org.ranch.ballshack.gui.windows.widgets.Widget;

import java.util.function.Supplier;

public abstract class ModuleSetting<T, SELF extends ModuleSetting<T, SELF>> implements ISetting<T>, Dependency {

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
		setDependency(condition);
		return self();
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
		ModuleSettingSaver.markDirty();
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

	public Supplier<Boolean> getDependency() {
		return dependencyCondition;
	}

	public void setDependency(Supplier<Boolean> condition) {
		this.dependencyCondition = condition;
	}

	public abstract Widget getWidget(int x, int y, int width, int height);

	public abstract String getFormattedValue();

	public abstract JsonElement getJson();

	public abstract void readJson(JsonElement jsonElement);
}
