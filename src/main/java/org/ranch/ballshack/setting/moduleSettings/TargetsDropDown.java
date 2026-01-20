package org.ranch.ballshack.setting.moduleSettings;

import org.ranch.ballshack.util.EntityUtil;

public class TargetsDropDown extends DropDown {

	private final SettingToggle friends = settings.add(new SettingToggle(false, "Friends"));
	private final SettingToggle players = settings.add(new SettingToggle(true, "Players"));
	private final SettingToggle monsters = settings.add(new SettingToggle(true, "Monsters"));
	private final SettingToggle passive = settings.add(new SettingToggle(false, "Passive"));
	private final SettingToggle other = settings.add(new SettingToggle(false, "Other"));

	public TargetsDropDown(String label) {
		super(label);
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
