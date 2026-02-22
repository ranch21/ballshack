package org.ranch.ballshack.setting;

import java.util.function.Supplier;

public interface Dependency {
	default boolean dependencyMet() {
		if (getDependency() == null)
			return true;
		return getDependency().get();
	}

	Supplier<Boolean> getDependency();

	void setDependency(Supplier<Boolean> condition);
}
