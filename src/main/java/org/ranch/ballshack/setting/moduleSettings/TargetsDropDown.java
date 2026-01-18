package org.ranch.ballshack.setting.moduleSettings;

import org.ranch.ballshack.util.EntityUtil;

import java.util.Arrays;

public class TargetsDropDown extends DropDown {

	public TargetsDropDown(String label) {
		super(label, Arrays.asList(
				new SettingToggle(false, "Friends"),
				new SettingToggle(true, "Players"),
				new SettingToggle(true, "Monsters"),
				new SettingToggle(false, "Passive"),
				new SettingToggle(false, "Other")
		));
	}

	public boolean getFriends() {
		return (boolean) getSetting(0).getValue();
	}

	public boolean getPlayers() {
		return (boolean) getSetting(1).getValue();
	}

	public boolean getMobs() {
		return (boolean) getSetting(2).getValue();
	}

	public boolean getPassive() {
		return (boolean) getSetting(3).getValue();
	}

	public boolean getOther() {
		return (boolean) getSetting(4).getValue();
	}

	public boolean selected(EntityUtil.EntityType type) {
		switch (type) {
			case PASSIVE -> {
				if (!getPassive()) return false;
			}
			case MONSTER, NEUTRAL -> {
				if (!getMobs()) return false;
			}
			case PLAYER, FRIEND -> {
				if (!getPlayers()) return false;
			}
			case OTHER -> {
				if (!getOther()) return false;
			}
		}
		return true;
	}
}
