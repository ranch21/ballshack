package org.ranch.ballshack.setting;

public class Setting<T> {
	private T value;
	private String key;

	public Setting(T value, String key) {
		this.value = value;
		this.key = key;
	}

	public T getValue() {
		return value;
	}

	public String getKey() {
		return key;
	}

	public void setValue(T value) {
		this.value = value;
		SettingSaver.SCHEDULE_SAVE.set(true);
	}
}
