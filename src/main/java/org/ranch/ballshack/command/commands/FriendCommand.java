package org.ranch.ballshack.command.commands;

import org.ranch.ballshack.FriendManager;
import org.ranch.ballshack.command.Command;

import java.util.List;

public class FriendCommand extends Command {
	public FriendCommand() {
		super("friends");
	}

	@Override
	public void onCall(String[] args) {
		if (args.length <= 1) {
			log("Please provide an action (add, remove, clear, list)", true);
			return;
		}
		if (args.length > 2) {
			switch (args[1]) {
				case "add":
					if (FriendManager.add(args[2])) {
						log("Added friend: " + args[2], true);
					}
					break;
				case "remove":
					if (FriendManager.remove(args[2])) {
						log("Removed friend: " + args[2], true);
					}
					break;
				case "clear":
					FriendManager.clear();
					log("Cleared friends :(", true);
					break;
				case "list":
					List<String> friendsList = FriendManager.getFriends();
					if (friendsList.isEmpty()) {
						log("No friends :(", true);
					}
					log("Friends:", true);
					for (String friend : friendsList) {
						log(friend, true);
					}
					break;
				default:
					log("Please provide a valid action (add, remove, clear)", true);
			}
		}
	}
}
