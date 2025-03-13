package org.ranch.ballshack;

import org.ranch.ballshack.setting.SettingSaver;

import java.util.ArrayList;
import java.util.List;

public class FriendManager {

	private static List<String> friends = new ArrayList<>();

	public static boolean add(String name) {
		name = name.toLowerCase();
		if (!friends.contains(name)) {
			friends.add(name);
			SettingSaver.SCHEDULE_SAVE.set(true);
			return true;
		}
		return false;
	}

	public static boolean remove(String name) {
		name = name.toLowerCase();
		if (friends.contains(name)) {
			String finalName = name;
			friends.removeIf(s -> s.equals(finalName));
			SettingSaver.SCHEDULE_SAVE.set(true);
			return true;
		}
		return false;
	}

	public static void clear() {
		friends.clear();
		SettingSaver.SCHEDULE_SAVE.set(true);
	}

	public static boolean has(String name) {
		return friends.contains(name);
	}

	public static List<String> getFriends() {
		return friends;
	}
}
