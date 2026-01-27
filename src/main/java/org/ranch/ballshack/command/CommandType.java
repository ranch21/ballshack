package org.ranch.ballshack.command;

public enum CommandType {
	CLIENT, // does not interact with server
	SURVIVAL, // does something with server but does not require creative
	CREATIVE // requires creative
}
