package org.ranch.ballshack.gui.neko;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.NoWorld;
import org.ranch.ballshack.event.events.EventHudRender;
import org.ranch.ballshack.event.events.EventScreen;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;

public class Neko {

	// movement
	private static final Identifier UP1 = texture("up1");
	private static final Identifier UP2 = texture("up2");
	private static final Identifier[] UP = {UP1, UP2};

	private static final Identifier UP_RIGHT1 = texture("upright1");
	private static final Identifier UP_RIGHT2 = texture("upright2");
	private static final Identifier[] UP_RIGHT = {UP_RIGHT1, UP_RIGHT2};

	private static final Identifier RIGHT1 = texture("right1");
	private static final Identifier RIGHT2 = texture("right2");
	private static final Identifier[] RIGHT = {RIGHT1, RIGHT2};

	private static final Identifier DOWN_RIGHT1 = texture("downright1");
	private static final Identifier DOWN_RIGHT2 = texture("downright2");
	private static final Identifier[] DOWN_RIGHT = {DOWN_RIGHT1, DOWN_RIGHT2};

	private static final Identifier DOWN1 = texture("down1");
	private static final Identifier DOWN2 = texture("down2");
	private static final Identifier[] DOWN = {DOWN1, DOWN2};

	private static final Identifier DOWN_LEFT1 = texture("downleft1");
	private static final Identifier DOWN_LEFT2 = texture("downleft2");
	private static final Identifier[] DOWN_LEFT = {DOWN_LEFT1, DOWN_LEFT2};

	private static final Identifier LEFT1 = texture("left1");
	private static final Identifier LEFT2 = texture("left2");
	private static final Identifier[] LEFT = {LEFT1, LEFT2};

	private static final Identifier UP_LEFT1 = texture("upleft1");
	private static final Identifier UP_LEFT2 = texture("upleft2");
	private static final Identifier[] UP_LEFT = {UP_LEFT1, UP_LEFT2};

	// actions
	private static final Identifier AWAKE = texture("awake");

	private static final Identifier UP_CLAW1 = texture("upclaw1");
	private static final Identifier UP_CLAW2 = texture("upclaw2");
	private static final Identifier[] UP_CLAW = {UP_CLAW1, UP_CLAW2};

	private static final Identifier RIGHT_CLAW1 = texture("rightclaw1");
	private static final Identifier RIGHT_CLAW2 = texture("rightclaw2");
	private static final Identifier[] RIGHT_CLAW = {RIGHT_CLAW1, RIGHT_CLAW2};

	private static final Identifier DOWN_CLAW1 = texture("downclaw1");
	private static final Identifier DOWN_CLAW2 = texture("downclaw2");
	private static final Identifier[] DOWN_CLAW = {DOWN_CLAW1, DOWN_CLAW2};

	private static final Identifier LEFT_CLAW1 = texture("leftclaw1");
	private static final Identifier LEFT_CLAW2 = texture("leftclaw2");
	private static final Identifier[] LEFT_CLAW = {LEFT_CLAW1, LEFT_CLAW2};

	private static final Identifier SCRATCH_CLAW1 = texture("scratch1");
	private static final Identifier SCRATCH_CLAW2 = texture("scratch2");
	private static final Identifier[] SCRATCH_CLAW = {SCRATCH_CLAW1, SCRATCH_CLAW2};

	private static final Identifier SLEEP1 = texture("sleep1");
	private static final Identifier SLEEP2 = texture("sleep2");
	private static final Identifier[] SLEEP = {SLEEP1, SLEEP2};

	private static final Identifier WASH1 = texture("wash1");
	private static final Identifier WASH2 = texture("wash2");
	private static final Identifier[] WASH = {WASH1, WASH2};

	private static final Identifier YAWN1 = texture("yawn1");
	private static final Identifier YAWN2 = texture("yawn2");
	private static final Identifier[] YAWN = {YAWN1, YAWN2};

	private static final int TEXTURE_SIZE = 32;
	private static final int SIZE = TEXTURE_SIZE / 2;

	private static Identifier texture(String name) {
		return Identifier.of(BallsHack.ID, "textures/neko/" + name + ".png");
	}

	private enum Direction {
		UP, UP_RIGHT,
		RIGHT, DOWN_RIGHT,
		DOWN, DOWN_LEFT,
		LEFT, UP_LEFT;

		public static Direction fromAngle(float radians) {
			final float TWO_PI = (float) (Math.PI * 2);

			float angle = radians % TWO_PI;
			if (angle < 0) {
				angle += TWO_PI;
			}

			int sector = Math.round(angle / (float)(Math.PI / 4)) % 8;

			switch (sector) {
				case 0: return RIGHT;
				case 1: return DOWN_RIGHT;
				case 2: return DOWN;
				case 3: return DOWN_LEFT;
				case 4: return LEFT;
				case 5: return UP_LEFT;
				case 6: return UP;
				case 7: return UP_RIGHT;
				default: return null;
			}
		}
	}

	private enum State {
		WALKING,
		SITTING,
		WASHING,
		SCRATCHING,
		YAWNING,
		AWAKE,
		SLEEPING
	}

	private static final float SPEED = 5f;
	private static final float DEFAULT_ANIM_SPEED = 3f;
	private static final float REST_DISTANCE = 10f;
	private float animSpeed = DEFAULT_ANIM_SPEED;
	private float life = 0.0f;
	private float stateStart;
	private boolean tookStep;
	private final Vector2f pos;
	private State state;

	public Neko() {
		this.pos = new Vector2f(-SIZE, -SIZE);
		this.stateStart = life;
		this.tookStep = false;
		this.state = State.WALKING;
	}

	@NoWorld
	@EventSubscribe
	public void onScreenRender(EventScreen.Render event) {
		update(event.tickDelta, event.mouseX, event.mouseY);
		draw(event.drawContext, event.mouseX, event.mouseY);
	}

	@EventSubscribe
	public void onHudRender(EventHudRender.Post event) {
		if (BallsHack.mc.currentScreen != null) return;
		int y = event.drawContext.getScaledWindowHeight() - 20;
		update(event.tickCounter.getDynamicDeltaTicks(), getSlotX(event), y);
		if (Math.abs(pos.y - y) < SPEED * 2)
			pos.y = y - SIZE / 2;
		draw(event.drawContext, getSlotX(event), y - SIZE / 2);
	}

	public void update(float tickDelta, int targetX, int targetY) {

		life += tickDelta;
		float stateLen = life - stateStart;

		switch (state) {
			case AWAKE:
				if (stateLen > 15f) setState(State.WALKING);
				break;
			case WALKING:
				animSpeed = DEFAULT_ANIM_SPEED;
				if (atTarget(targetX, targetY)) setState(State.SITTING);
				if (life % animSpeed > animSpeed / 2) {
					tookStep = false;
				}

				if (life % animSpeed > animSpeed / 2 || tookStep)
					return;

				Vector2f dir = new Vector2f(targetX - pos.x, targetY - pos.y);
				dir.normalize().mul(SPEED);
				pos.add(dir);
				tookStep = true;
				break;
			case SITTING:
				if (stateLen > 20f) setState(State.WASHING);
				if (!atTarget(targetX, targetY, REST_DISTANCE * 2)) setState(State.AWAKE);
				break;
			case WASHING:
				animSpeed = DEFAULT_ANIM_SPEED;
				if (stateLen > 30f) setState(State.SCRATCHING);
				if (!atTarget(targetX, targetY, REST_DISTANCE * 2)) setState(State.AWAKE);
				break;
			case SCRATCHING:
				animSpeed = DEFAULT_ANIM_SPEED;
				if (stateLen > 20f) setState(State.YAWNING);
				if (!atTarget(targetX, targetY, REST_DISTANCE * 2)) setState(State.AWAKE);
				break;
			case YAWNING:
				if (stateLen > 20f) setState(State.SLEEPING);
				if (!atTarget(targetX, targetY, REST_DISTANCE * 2)) setState(State.AWAKE);
				break;
			case SLEEPING:
				animSpeed = DEFAULT_ANIM_SPEED * 4;
				if (!atTarget(targetX, targetY, REST_DISTANCE * 2)) setState(State.AWAKE);
				break;
		}
	}

	public void setState(State state) {
		this.state = state;
		stateStart = life;
	}

	public boolean atTarget(int x, int y) {
		return atTarget(x, y, REST_DISTANCE);
	}

	public int getSlotX(EventHudRender.Post event) {
		int hotbarStart = event.drawContext.getScaledWindowWidth() / 2 - 91;
		return hotbarStart + BallsHack.mc.player.getInventory().getSelectedSlot() * 20 + 10;
	}

	public boolean atTarget(int x, int y, float distance) {
		return pos.distance(x, y) < distance;
	}

	public void draw(DrawContext context, int targetX, int targetY) {
		int frame = (int)(life / animSpeed) % 2;
		Identifier texture = Identifier.of("textures/gui/sprites/icon/unseen_notification.png");

		Direction dir = Direction.fromAngle(angle(targetX, targetY));

		if (dir == null) return;

		switch (state) {
			case WALKING:
				texture = switch (dir) {
					case UP -> UP[frame];
					case UP_RIGHT -> UP_RIGHT[frame];
					case RIGHT -> RIGHT[frame];
					case DOWN_RIGHT -> DOWN_RIGHT[frame];
					case DOWN -> DOWN[frame];
					case DOWN_LEFT -> DOWN_LEFT[frame];
					case LEFT -> LEFT[frame];
					case UP_LEFT -> UP_LEFT[frame];
				};
				break;
			case WASHING: texture = WASH[frame]; break;
			case SCRATCHING: texture = SCRATCH_CLAW[frame]; break;
			case YAWNING: texture = YAWN[1]; break;
			case SLEEPING: texture = SLEEP[frame]; break;
			case SITTING: texture = YAWN[0]; break;
			case AWAKE: texture = AWAKE; break;
			default:
		}

		context.drawTexture(
				RenderPipelines.GUI_TEXTURED, texture,
				(int)(pos.x - (float) SIZE / 2), (int)(pos.y - (float) SIZE / 2),
				0, 0,
				SIZE, SIZE,
				SIZE, SIZE
		);
	}

	public float angle(int mouseX, int mouseY) {
		return (float) Math.atan2(mouseY - pos.y, mouseX - pos.x);
	}
}
