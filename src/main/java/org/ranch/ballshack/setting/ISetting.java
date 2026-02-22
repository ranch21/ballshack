package org.ranch.ballshack.setting;

public interface ISetting<T> {
	T getValue();

	void setValue(T value);

	String getFormattedValue();
}
