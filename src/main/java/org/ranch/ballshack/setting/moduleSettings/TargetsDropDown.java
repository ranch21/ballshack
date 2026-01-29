package org.ranch.ballshack.setting.moduleSettings;

import org.ranch.ballshack.util.EntityUtil;

public class TargetsDropDown extends DropDown {

	private final SettingToggle friends = settings.add(new SettingToggle("Friends", false));
	private final SettingToggle players = settings.add(new SettingToggle("Players", true));
	private final SettingToggle monsters = settings.add(new SettingToggle("Monsters", true));
	private final SettingToggle passive = settings.add(new SettingToggle("Passive", false));
	private final SettingToggle other = settings.add(new SettingToggle("Other", false));

	public TargetsDropDown(String label) {
		super(label);
	}

	public TargetsDropDown friends(boolean b) {
		friends.setValue(b);
		return this;
	}

	public TargetsDropDown players(boolean b) {
		players.setValue(b);
		return this;
	}

	public TargetsDropDown monsters(boolean b) {
		monsters.setValue(b);
		return this;
	}

	public TargetsDropDown passive(boolean b) {
		passive.setValue(b);
		return this;
	}

	public TargetsDropDown other(boolean b) {
		other.setValue(b);
		return this;
	}

	public boolean getFriends() {
		return friends.getValue();
	}

	public boolean getPlayers() {
		return players.getValue();
	}

	public boolean getMobs() {
		return monsters.getValue();
	}

	public boolean getPassive() {
		return passive.getValue();
	}

	public boolean getOther() {
		return other.getValue();
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
