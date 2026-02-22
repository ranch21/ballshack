package org.ranch.ballshack.module.modules.render;

import com.google.common.collect.Streams;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventHudRender;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.TargetsSettingGroup;
import org.ranch.ballshack.setting.settings.BooleanSetting;
import org.ranch.ballshack.setting.settings.ModeSetting;
import org.ranch.ballshack.setting.settings.NumberSetting;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.InvUtil;
import org.ranch.ballshack.util.TextUtil;
import org.ranch.ballshack.util.rendering.DrawUtil;

import java.awt.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Nametags extends Module {

	public final NumberSetting minSize = dGroup.add(new NumberSetting("MinSize", 0.5).min(0.1).max(2).step(0.1).tooltip("(its not inverted)"));
	public final NumberSetting size = dGroup.add(new NumberSetting("Size", 4).min(0.5).max(8).step(0.5));
	public final BooleanSetting items = dGroup.add(new BooleanSetting("Items", true));
	public final BooleanSetting ping = dGroup.add(new BooleanSetting("Ping", true));
	public final TargetsSettingGroup targets = addGroup(new TargetsSettingGroup("Targets"));

	public static enum HealthMode {
		NONE, NUM, FRACTION
	}

	public final ModeSetting<HealthMode> health = dGroup.add(new ModeSetting<>("Health", HealthMode.FRACTION, HealthMode.values()));

	public Nametags() {
		super("Nametags", ModuleCategory.RENDER, 0, "wgat is your name?");
	}

	private Matrix4f prevMatrix = new Matrix4f();

	@EventSubscribe
	public void onMainWorldRender(EventWorldRender.Post event) {
		prevMatrix = RenderSystem.getModelViewMatrix();
	}

	@EventSubscribe
	public void onHudRender(EventHudRender.Pre event) {
		Stream<Entity> entities = Streams.stream(mc.world.getEntities()).sorted(Comparator.comparingDouble((ent) -> -ent.distanceTo(mc.player)));
		for (Entity e : entities.toList()) {
			if (e != mc.player) {
				EntityUtil.EntityType type = EntityUtil.getEntityType(e);

				if (!targets.selected(type)) continue;

				Vec3d pos = e.getLerpedPos(event.tickCounter.getTickProgress(true)).add(0, e.getHeight(), 0);

				Vector2f screenPos = toHudPos(DrawUtil.worldToScreen(pos, prevMatrix, event.tickCounter.getTickProgress(true)));
				if (screenPos == null) continue;

				float scale = (1 / (float) pos.distanceTo(mc.gameRenderer.getCamera().getCameraPos())) * size.getValueFloat();

				Matrix3x2fStack stack = event.drawContext.getMatrices();
				stack.pushMatrix();

				stack.translate(screenPos);
				drawNametag(event.drawContext, e, stack, Math.max(scale, minSize.getValueFloat()));

				stack.popMatrix();
			}
		}
	}

	public Vector2f toHudPos(Vector2f ndc) {
		if (ndc == null) return null;
		float x = (ndc.x + 1.0f) / 2.0f * DrawUtil.getScreenWidth();
		float y = (1.0f - ndc.y) / 2.0f * DrawUtil.getScreenHeight();
		return new Vector2f(x, y);
	}

	private void drawNametag(DrawContext context, Entity e, Matrix3x2fStack stack, float scale) {

		EntityUtil.EntityType type = EntityUtil.getEntityType(e);

		Text tagT = Text.empty().append(e.getName().copy().withColor(type.getColor().hashCode()));
		if ((health.getValue() == HealthMode.FRACTION || health.getValue() == HealthMode.NUM) && e instanceof LivingEntity le) {
			tagT = tagT.copy()
					.append(Text.literal(" " + TextUtil.formatDecimal(le.getHealth(), "#.#"))
							.withColor(EntityUtil.getHPCol(le).hashCode()));
		}
		if (health.getValue() == HealthMode.FRACTION && e instanceof LivingEntity le) {
			tagT = tagT.copy()
					.append(Text.literal("/")
							.formatted(Formatting.WHITE)).append(TextUtil.formatDecimal(le.getMaxHealth(), "#.#"))
					.withColor(Colors.DULL_GREEN.hashCode());
		}

		if (e instanceof OtherClientPlayerEntity pe && ping.getValue()) {
			Collection<PlayerListEntry> players = mc.player.networkHandler.getPlayerList();
			Optional<PlayerListEntry> entry = players.stream()
					.filter((p) -> p.getProfile().equals(pe.getGameProfile()))
					.findFirst();

			if (entry.isPresent())
				tagT = tagT.copy()
						.append(Text.literal(" " + entry.get().getLatency())
								.withColor(EntityUtil.getCol(-((float) entry.get().getLatency() / 300) + 1).hashCode()))
						.append(Text.literal("ms").formatted(Formatting.GRAY));
		}

		stack.scale(scale, scale);
		stack.translate((float) -mc.textRenderer.getWidth(tagT) / 2, -(float) mc.textRenderer.fontHeight / 2);

		DrawUtil.drawOutlineWithCorners(context, -2, -2, mc.textRenderer.getWidth(tagT) + 3, mc.textRenderer.fontHeight + 2, Color.DARK_GRAY, Color.DARK_GRAY);
		context.fill(-1, -1, mc.textRenderer.getWidth(tagT) + 1, mc.textRenderer.fontHeight, Colors.HUD_BACKGROUND.getColor().hashCode());
		context.drawText(mc.textRenderer, tagT, 0, 0, 0xFFFFFFFF, true);

		if (e instanceof LivingEntity le && items.getValue()) {
			drawSlots(context, mc.textRenderer.getWidth(tagT) / 2, -20, le);
		}
	}

	private void drawSlots(DrawContext context, int x, int y, LivingEntity le) {
		List<ItemStack> armorItems = InvUtil.getArmorSlots(le);
		armorItems.removeIf(ItemStack::isEmpty);
		ItemStack mh = le.getMainHandStack();
		ItemStack oh = le.getOffHandStack();
		int w = armorItems.size() * 8;
		if (!mh.isEmpty()) w += 8;
		if (!oh.isEmpty()) w += 8;
		int j = 0;
		if (!mh.isEmpty()) {
			drawItem(context, x - w, y, mh, Colors.DULL_GREEN);
			j++;
		}
		for (int i = armorItems.size() - 1; i >= 0; i--) {
			drawItem(context, x - w + (j * 16), y, armorItems.get(i), Color.ORANGE);
			j++;
		}
		if (!oh.isEmpty()) drawItem(context, x - w + (j * 16), y, oh, Colors.DULL_BLUE);
	}

	private void drawItem(DrawContext context, int x, int y, ItemStack stack, Color color) {
		context.drawItem(stack, x, y);
		context.drawStackOverlay(mc.textRenderer, stack, x, y);
		context.drawHorizontalLine(x + 1, x + 15, y + 16, color.hashCode());
	}
}