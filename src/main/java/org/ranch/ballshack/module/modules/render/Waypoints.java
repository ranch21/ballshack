package org.ranch.ballshack.module.modules.render;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventHudRender;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.gui.windows.RemovalReason;
import org.ranch.ballshack.gui.windows.Window;
import org.ranch.ballshack.gui.windows.widgets.ButtonWidget;
import org.ranch.ballshack.gui.windows.widgets.ListWidget;
import org.ranch.ballshack.gui.windows.widgets.Widget;
import org.ranch.ballshack.gui.windows.widgets.setting.SettingWidget;
import org.ranch.ballshack.gui.windows.widgets.setting.StringWidget;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.ISetting;
import org.ranch.ballshack.setting.module.ModuleSetting;
import org.ranch.ballshack.setting.module.ModuleSettingsGroup;
import org.ranch.ballshack.setting.module.settings.NumberSetting;
import org.ranch.ballshack.setting.module.settings.StringSetting;
import org.ranch.ballshack.util.TextUtil;
import org.ranch.ballshack.util.rendering.BallColor;
import org.ranch.ballshack.util.rendering.DrawUtil;
import org.ranch.ballshack.util.rendering.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Waypoints extends Module {

	public final NumberSetting minSize = dGroup.num("MinSize", 0.5).min(0.5).max(2).step(0.1);
	public final NumberSetting size = dGroup.num("Size", 8).min(4).max(16).step(1);

	public ModuleSettingsGroup coordListGroup = addGroup(new ModuleSettingsGroup("Waypoints"));
	public CoordinateListSetting list = coordListGroup.add(new CoordinateListSetting("Coords", new ArrayList<>()).label(false));

	private Matrix4f prevMatrix = new Matrix4f();

	public Waypoints() {
		super("Waypoints", ModuleCategory.RENDER, 0);
	}

	@EventSubscribe
	public void onWorldRender(EventWorldRender.Post event) {
		Renderer renderer = Renderer.getInstance();

		MatrixStack matrices = event.matrixStack;

		for (Waypoint waypoint : list.getValue()) {
			renderer.queueLine(waypoint.pos.toCenterPos(), waypoint.pos.toCenterPos().add(0, 1000, 0), BallColor.of(waypoint.color), matrices);
		}

		prevMatrix = RenderSystem.getModelViewMatrix();
	}

	@EventSubscribe
	public void onHudRender(EventHudRender.Pre event) {
		for (Waypoint waypoint : list.getValue()) {

			Vec3d pos = waypoint.pos.toCenterPos();

			Vector2f screenPos = DrawUtil.toHudPos(DrawUtil.worldToScreen(pos, prevMatrix, event.tickCounter.getTickProgress(true)));
			if (screenPos == null) continue;

			float scale = (1 / (float) pos.distanceTo(mc.gameRenderer.getCamera().getCameraPos())) * size.getValueFloat();

			Matrix3x2fStack stack = event.drawContext.getMatrices();
			stack.pushMatrix();

			stack.translate(screenPos);
			drawWaypoint2D(event.drawContext, waypoint, stack, Math.max(scale, minSize.getValueFloat()));

			stack.popMatrix();
		}
	}

	private void drawWaypoint2D(DrawContext context, Waypoint w, Matrix3x2fStack stack, float scale) {
		Text wt = Text.empty().append(Text.literal(w.name).copy().withColor(w.color.hashCode()));

		wt = wt.copy().append(Text.literal(" " + TextUtil.formatDecimal(mc.player.getEntityPos().distanceTo(w.pos.toCenterPos()), "#") + "m"));

		stack.scale(scale, scale);
		stack.translate((float) -mc.textRenderer.getWidth(wt) / 2, -(float) mc.textRenderer.fontHeight / 2);

		DrawUtil.drawOutlineWithCorners(context, -1, -1, mc.textRenderer.getWidth(wt) + 2, mc.textRenderer.fontHeight + 1, Color.DARK_GRAY, Color.DARK_GRAY);
		context.fill(-1, -1, mc.textRenderer.getWidth(wt) + 1, mc.textRenderer.fontHeight, Colors.HUD_BACKGROUND.getColor().hashCode());
		context.drawText(mc.textRenderer, wt, 0, 0, 0xFFFFFFFF, true);
	}

	public static class Waypoint {

		public BlockPos pos;
		public String name;
		public Color color;

		public Waypoint(String name, Color color, BlockPos pos) {
			this.name = name;
			this.color = color;
			this.pos = pos;
		}

		public int getX() {
			return pos.getX();
		}

		public int getY() {
			return pos.getY();
		}

		public int getZ() {
			return pos.getZ();
		}
	}

	public static class CoordinateListSetting extends ModuleSetting<List<Waypoint>, CoordinateListSetting> {
		public CoordinateListSetting(String name, List<Waypoint> value) {
			super(name, value);
		}

		@Override
		public Widget getWidget(int x, int y, int width, int height) {
			return new CoordinateListWidget(getName(), x, y, width, 100, this);
		}

		@Override
		public String getFormattedValue() {
			return "";
		}

		@Override
		public JsonElement getJson() {
			JsonArray array = new JsonArray();
			getValue().forEach((waypoint) -> {
				JsonObject obj = new JsonObject();
				obj.addProperty("name", waypoint.name);
				obj.addProperty("color", waypoint.color.getRGB());
				obj.addProperty("x", waypoint.getX());
				obj.addProperty("y", waypoint.getY());
				obj.addProperty("z", waypoint.getZ());
				array.add(obj);
			});
			return array;
		}

		@Override
		public void readJson(JsonElement jsonElement) {
			List<Waypoint> posList = new ArrayList<>();
			jsonElement.getAsJsonArray().forEach((element) -> {
				JsonObject obj = element.getAsJsonObject();
				posList.add(new Waypoint(
						obj.get("name").getAsString(),
						new Color(obj.get("color").getAsInt()),
						new BlockPos(
								obj.get("x").getAsInt(),
								obj.get("y").getAsInt(),
								obj.get("z").getAsInt()
						)
				));
			});
			setValue(posList);
		}
	}

	public static class CoordinateListWidget extends SettingWidget<List<Waypoint>> implements ListWidget {

		public CoordinateListWidget(String title, int x, int y, int width, int height, ISetting<List<Waypoint>> setting) {
			super(title, x, y, width, height, setting);
			addFlags(INDENTED);
		}

		@Override
		public void render(DrawContext context, double mouseX, double mouseY, float delta) { // get this man an imgui
			for (Window win : getChildren()) {
				if (win instanceof CoordListEntry entry) {
					if (!setting.getValue().contains(entry.waypoint)) win.remove(RemovalReason.KILLED);
				}
			}
			purgeDead();
			for (Waypoint waypoint : setting.getValue()) {
				boolean has = false;
				for (Window window : getChildren()) {
					if (window instanceof CoordListEntry entry) {
						if (entry.waypoint.equals(waypoint)) {
							has = true;
						}
					}
				}
				if (!has) {
					addChild(new CoordListEntry(waypoint.name, 0, 0, 10, 27, waypoint, () -> {
						setting.getValue().remove(waypoint);
					}));
				}
			}
			arrangeChildren();
			super.render(context, mouseX, mouseY, delta);
		}
	}

	public static class CoordListEntry extends Widget {

		public Waypoint waypoint;
		private StringSetting nameSetting; //ungodly
		private StringSetting xStrSetting;
		private StringSetting yStrSetting;
		private StringSetting zStrSetting;
		private boolean invalid;
		private Runnable onDelete;

		public CoordListEntry(String title, int x, int y, int width, int height, Waypoint waypoint, Runnable onDelete) {
			super(title, x, y, width, height);
			this.waypoint = waypoint;
			invalid = false;
			this.onDelete = onDelete;
		}

		@Override
		public void init() {
			super.init();

			int w = 29;

			nameSetting = new StringSetting("name", waypoint.name);
			xStrSetting = new StringSetting("x", Integer.toString(waypoint.getX()));
			yStrSetting = new StringSetting("y", Integer.toString(waypoint.getY()));
			zStrSetting = new StringSetting("z", Integer.toString(waypoint.getZ()));

			addChild(new StringWidget("name", 2, 2, w * 3, 10, false, nameSetting));

			addChild(new ButtonWidget("r", w + w + w + 5, 15, 10, 10, (win, click) -> {
				if (mc.world == null) return;
				waypoint.pos = BlockPos.ofFloored(mc.player.getEntityPos());
				remove(RemovalReason.KILLED);
			}));

			addChild(new ButtonWidget("x", w + w + w + 5, 2, 10, 10, (win, click) -> {
				onDelete.run();
			}));

			addChild(new StringWidget("x", 2, 15, w, 10, false, xStrSetting) {
				@Override
				protected void drawBackground(DrawContext context, double mouseX, double mouseY, float delta) {
					context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), BallColor.of(Colors.DULL_RED).setAlpha(0.1f).hashCode());
				}
			});
			addChild(new StringWidget("y", w + 2, 15, w, 10, false, yStrSetting) {
				@Override
				protected void drawBackground(DrawContext context, double mouseX, double mouseY, float delta) {
					context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), BallColor.of(Colors.DULL_GREEN).setAlpha(0.1f).hashCode());
				}
			});
			addChild(new StringWidget("z", w + w + 2, 15, w, 10, false, zStrSetting) {
				@Override
				protected void drawBackground(DrawContext context, double mouseX, double mouseY, float delta) {
					super.drawBackground(context, mouseX, mouseY, delta);
					context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), BallColor.of(Colors.DULL_BLUE).setAlpha(0.1f).hashCode());
				}
			});
		}

		private void refresh() {
			nameSetting = new StringSetting("name", waypoint.name);
			xStrSetting = new StringSetting("x", Integer.toString(waypoint.getX()));
			yStrSetting = new StringSetting("y", Integer.toString(waypoint.getY()));
			zStrSetting = new StringSetting("z", Integer.toString(waypoint.getZ()));
		}

		@Override
		public void render(DrawContext context, double mouseX, double mouseY, float delta) {

			try {
				waypoint.pos = new BlockPos(
						Integer.parseInt(xStrSetting.getValue()),
						Integer.parseInt(yStrSetting.getValue()),
						Integer.parseInt(zStrSetting.getValue())
				);
				waypoint.name = nameSetting.getValue();
				invalid = false;
			} catch (NumberFormatException e) {
				invalid = true;
			}

			super.render(context, mouseX, mouseY, delta);
		}

		@Override
		protected void drawOutline(DrawContext context, double mouseX, double mouseY, float delta) {
			if (invalid) {
				DrawUtil.drawOutline(
						context,
						getX(), getY() - ((style & NO_TITLE) == 0 ? BAR_HEIGHT : 0),
						getWidth(), getHeight() + ((style & NO_TITLE) == 0 ? BAR_HEIGHT : 0),
						Colors.DULL_RED, Colors.DULL_RED
				);
			} else {
				DrawUtil.drawOutline(
						context,
						getX(), getY() - ((style & NO_TITLE) == 0 ? BAR_HEIGHT : 0),
						getWidth(), getHeight() + ((style & NO_TITLE) == 0 ? BAR_HEIGHT : 0)
				);
			}
		}
	}
}
