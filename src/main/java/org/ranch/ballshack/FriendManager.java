package org.ranch.ballshack;

import org.ranch.ballshack.setting.client.ClientSetting;

import java.util.ArrayList;
import java.util.List;

public class FriendManager {

	public static final ClientSetting<List<String>> setting = new ClientSetting<>("friends", new ArrayList<>());

	public static boolean add(String name) {
		name = name.toLowerCase();
		if (!setting.getValue().contains(name)) {
			setting.getValue().add(name);
			return true;
		}
		return false;
	}

	public static boolean remove(String name) {
		name = name.toLowerCase();
		if (setting.getValue().contains(name)) {
			String finalName = name;
			setting.getValue().removeIf(s -> s.equals(finalName));
			return true;
		}
		return false;
	}

	public static void clear() {
		setting.getValue().clear();
	}

	public static boolean has(String name) {
		return setting.getValue().contains(name.toLowerCase());
	}

	public static List<String> getFriends() {
		return setting.getValue();
	}
}
