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
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.module.modules.client.NekoModule;

import static org.ranch.ballshack.gui.neko.NekoTextures.*;

public class Neko {

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

			int sector = Math.round(angle / (float) (Math.PI / 4)) % 8;

			switch (sector) {
				case 0:
					return RIGHT;
				case 1:
					return DOWN_RIGHT;
				case 2:
					return DOWN;
				case 3:
					return DOWN_LEFT;
				case 4:
					return LEFT;
				case 5:
					return UP_LEFT;
				case 6:
					return UP;
				case 7:
					return UP_RIGHT;
				default:
					return null;
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

	private static final float DEFAULT_SPEED = 5f;
	private static final float DEFAULT_ANIM_SPEED = 3f;
	private static final float REST_DISTANCE = 10f;
	private float animSpeed = DEFAULT_ANIM_SPEED;
	private int size = TEXTURE_SIZE;
	private float life = 0.0f;
	private float stateStart;
	private boolean tookStep;
	private final Vector2f pos;
	private State state;

	public Neko() {
		this.pos = new Vector2f(-size, -size);
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
		int y = event.drawContext.getScaledWindowHeight() - 18;
		update(event.tickCounter.getDynamicDeltaTicks(), getSlotX(event), y);
		if (Math.abs(pos.y - y) < getSpeed() * 2)
			pos.y = y - size / 2;
		draw(event.drawContext, getSlotX(event), y - size / 2);
	}

	public void update(float tickDelta, int targetX, int targetY) {

		size = ((NekoModule) ModuleManager.getModuleByName("Neko")).size.getValueInt();

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
				dir.normalize().mul(getSpeed());
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

	public float getSpeed() {
		return DEFAULT_SPEED * ((float) size / 16);
	}

	public boolean atTarget(int x, int y) {
		return atTarget(x, y, REST_DISTANCE * ((float) size / 16));
	}

	public int getSlotX(EventHudRender.Post event) {
		int hotbarStart = event.drawContext.getScaledWindowWidth() / 2 - 91;
		return hotbarStart + BallsHack.mc.player.getInventory().getSelectedSlot() * 20 + 10;
	}

	public boolean atTarget(int x, int y, float distance) {
		return pos.distance(x, y) < distance;
	}

	public void draw(DrawContext context, int targetX, int targetY) {
		int frame = (int) (life / animSpeed) % 2;
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
			case WASHING:
				texture = WASH[frame];
				break;
			case SCRATCHING:
				texture = SCRATCH_CLAW[frame];
				break;
			case YAWNING:
				texture = YAWN[1];
				break;
			case SLEEPING:
				texture = SLEEP[frame];
				break;
			case SITTING:
				texture = YAWN[0];
				break;
			case AWAKE:
				texture = AWAKE;
				break;
			default:
		}

		context.drawTexture(
				RenderPipelines.GUI_TEXTURED, texture,
				(int) (pos.x - (float) size / 2), (int) (pos.y - (float) size / 2),
				0, 0,
				size, size,
				size, size
		);
	}

	public float angle(int mouseX, int mouseY) {
		return (float) Math.atan2(mouseY - pos.y, mouseX - pos.x);
	}
}
