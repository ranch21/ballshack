package org.ranch.ballshack.setting.client;

import org.ranch.ballshack.setting.ISetting;

public class ClientSetting<T> implements ISetting<T> {
	private T value;
	private final String key;
	private String tooltip;

	public ClientSetting(String key, T value) {
		this.value = value;
		this.key = key;
		this.tooltip = "";
	}

	public ClientSetting<T> tooltip(String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	public T getValue() {
		return value;
	}

	public String getKey() {
		return key;
	}

	public void setValue(T value) {
		this.value = value;
		ClientSettingSaver.markDirty();
	}

	@SuppressWarnings("unchecked")
	public void setRawValue(Object value) {
		this.value = (T) value;
	}

	@Override
	public String getFormattedValue() {
		return String.valueOf(value);
	}

	@Override
	public String getTooltip() {
		return tooltip;
	}
}
