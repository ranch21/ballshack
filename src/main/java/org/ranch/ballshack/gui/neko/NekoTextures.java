package org.ranch.ballshack.gui.neko;

import net.minecraft.util.Identifier;
import org.ranch.ballshack.BallsHack;

public class NekoTextures {
	// movement
	public static final Identifier UP1 = texture("up1");
	public static final Identifier UP2 = texture("up2");
	public static final Identifier[] UP = {UP1, UP2};

	public static final Identifier UP_RIGHT1 = texture("upright1");
	public static final Identifier UP_RIGHT2 = texture("upright2");
	public static final Identifier[] UP_RIGHT = {UP_RIGHT1, UP_RIGHT2};

	public static final Identifier RIGHT1 = texture("right1");
	public static final Identifier RIGHT2 = texture("right2");
	public static final Identifier[] RIGHT = {RIGHT1, RIGHT2};

	public static final Identifier DOWN_RIGHT1 = texture("downright1");
	public static final Identifier DOWN_RIGHT2 = texture("downright2");
	public static final Identifier[] DOWN_RIGHT = {DOWN_RIGHT1, DOWN_RIGHT2};

	public static final Identifier DOWN1 = texture("down1");
	public static final Identifier DOWN2 = texture("down2");
	public static final Identifier[] DOWN = {DOWN1, DOWN2};

	public static final Identifier DOWN_LEFT1 = texture("downleft1");
	public static final Identifier DOWN_LEFT2 = texture("downleft2");
	public static final Identifier[] DOWN_LEFT = {DOWN_LEFT1, DOWN_LEFT2};

	public static final Identifier LEFT1 = texture("left1");
	public static final Identifier LEFT2 = texture("left2");
	public static final Identifier[] LEFT = {LEFT1, LEFT2};

	public static final Identifier UP_LEFT1 = texture("upleft1");
	public static final Identifier UP_LEFT2 = texture("upleft2");
	public static final Identifier[] UP_LEFT = {UP_LEFT1, UP_LEFT2};

	// actions
	public static final Identifier AWAKE = texture("awake");

	public static final Identifier UP_CLAW1 = texture("upclaw1");
	public static final Identifier UP_CLAW2 = texture("upclaw2");
	public static final Identifier[] UP_CLAW = {UP_CLAW1, UP_CLAW2};

	public static final Identifier RIGHT_CLAW1 = texture("rightclaw1");
	public static final Identifier RIGHT_CLAW2 = texture("rightclaw2");
	public static final Identifier[] RIGHT_CLAW = {RIGHT_CLAW1, RIGHT_CLAW2};

	public static final Identifier DOWN_CLAW1 = texture("downclaw1");
	public static final Identifier DOWN_CLAW2 = texture("downclaw2");
	public static final Identifier[] DOWN_CLAW = {DOWN_CLAW1, DOWN_CLAW2};

	public static final Identifier LEFT_CLAW1 = texture("leftclaw1");
	public static final Identifier LEFT_CLAW2 = texture("leftclaw2");
	public static final Identifier[] LEFT_CLAW = {LEFT_CLAW1, LEFT_CLAW2};

	public static final Identifier SCRATCH_CLAW1 = texture("scratch1");
	public static final Identifier SCRATCH_CLAW2 = texture("scratch2");
	public static final Identifier[] SCRATCH_CLAW = {SCRATCH_CLAW1, SCRATCH_CLAW2};

	public static final Identifier SLEEP1 = texture("sleep1");
	public static final Identifier SLEEP2 = texture("sleep2");
	public static final Identifier[] SLEEP = {SLEEP1, SLEEP2};

	public static final Identifier WASH1 = texture("wash1");
	public static final Identifier WASH2 = texture("wash2");
	public static final Identifier[] WASH = {WASH1, WASH2};

	public static final Identifier YAWN1 = texture("yawn1");
	public static final Identifier YAWN2 = texture("yawn2");
	public static final Identifier[] YAWN = {YAWN1, YAWN2};

	public static final int TEXTURE_SIZE = 32;

	public static Identifier texture(String name) {
		return Identifier.of(BallsHack.ID, "textures/neko/" + name + ".png");
	}
}
