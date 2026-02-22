package org.ranch.ballshack.setting;

import java.lang.reflect.Type;

public class Setting<T> implements ISetting<T> {
	private T value;
	private final String key;
	private final Type type;

	public Setting(T value, String key, Type type) {
		this.value = value;
		this.key = key;
		this.type = type;
	}

	public T getValue() {
		return value;
	}

	public String getKey() {
		return key;
	}

	public Type getType() {
		return type;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public String getFormattedValue() {
		return String.valueOf(value);
	}
}
