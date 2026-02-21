package org.ranch.ballshack.setting;

import org.ranch.ballshack.setting.settings.BooleanSetting;
import org.ranch.ballshack.util.EntityUtil;

public class TargetsSettingGroup extends ModuleSettingsGroup {

	private final BooleanSetting players = add(new BooleanSetting("Players", true));
	private final BooleanSetting friends = add(new BooleanSetting("Friends", false));
	private final BooleanSetting passive = add(new BooleanSetting("Passive", false));
	private final BooleanSetting neutral = add(new BooleanSetting("Neutral", false));
	private final BooleanSetting monsters = add(new BooleanSetting("Monsters", true));
	private final BooleanSetting other = add(new BooleanSetting("Other", false));

	public TargetsSettingGroup(String name) {
		super(name);
	}

	public boolean selected(EntityUtil.EntityType type) {
		switch (type) {
			case PASSIVE -> {
				if (!passive.getValue()) return false;
			}
			case MONSTER -> {
				if (!monsters.getValue()) return false;
			}
			case NEUTRAL -> {
				if (!neutral.getValue()) return false;
			}
			case FRIEND -> {
				if (!friends.getValue()) return false;
			}
			case PLAYER -> {
				if (!players.getValue()) return false;
			}
			case OTHER -> {
				if (!other.getValue()) return false;
			}
		}
		return true;
	}
}
