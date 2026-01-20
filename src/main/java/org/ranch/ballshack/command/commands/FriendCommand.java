package org.ranch.ballshack.command.commands;

import org.ranch.ballshack.FriendManager;
import org.ranch.ballshack.command.Command;

import java.util.List;

public class FriendCommand extends Command {
	public FriendCommand() {
		super("friends", "Used to modify friends list", "friends (add|remove) <username> | friends (clear|list)");
	}

	@Override
	public void onCall(int argc, String[] argv) {
		if (argv.length <= 1) {
			log("Please provide an action (add, remove, clear, list)", true);
			return;
		}
		if (argv.length > 2) {
			switch (argv[1]) {
				case "add":
					if (FriendManager.add(argv[2])) {
						log("Added friend: " + argv[2], true);
					}
					break;
				case "remove":
					if (FriendManager.remove(argv[2])) {
						log("Removed friend: " + argv[2], true);
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
