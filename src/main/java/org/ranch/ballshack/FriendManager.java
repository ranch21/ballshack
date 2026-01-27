package org.ranch.ballshack;

import com.google.gson.reflect.TypeToken;
import org.ranch.ballshack.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public class FriendManager {

	private static List<String> friends = new ArrayList<>();
	public static final Setting<List<String>> setting = new Setting<>(friends, "friends", new TypeToken<List<String>>() {
	}.getType());

	public static boolean add(String name) {
		name = name.toLowerCase();
		if (!friends.contains(name)) {
			friends.add(name);
			setting.setValue(friends);
			return true;
		}
		return false;
	}

	public static boolean remove(String name) {
		name = name.toLowerCase();
		if (friends.contains(name)) {
			String finalName = name;
			friends.removeIf(s -> s.equals(finalName));
			setting.setValue(friends);

			return true;
		}
		return false;
	}

	public static void clear() {
		friends.clear();
		setting.setValue(friends);
	}

	public static void set() {
		friends = setting.getValue();
	}

	public static boolean has(String name) {
		return friends.contains(name);
	}

	public static List<String> getFriends() {
		return friends;
	}
}
