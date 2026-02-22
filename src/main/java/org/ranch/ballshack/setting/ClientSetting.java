package org.ranch.ballshack.setting;

public class ClientSetting<T> implements ISetting<T> {
	private T value;
	private final String key;

	public ClientSetting(String key, T value) {
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
}
