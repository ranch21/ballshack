package org.ranch.ballshack.setting.settings;

import java.util.Arrays;

public class TargetsDropDown extends DropDown {

	public TargetsDropDown(String label) {
		super(label, Arrays.asList(
				new SettingToggle(false, "Friends"),
				new SettingToggle(true, "Players"),
				new SettingToggle(true, "Monsters"),
				new SettingToggle(false, "Passive")
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
}
