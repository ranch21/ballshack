package org.ranch.ballshack.module.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventWorldRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingMode;
import org.ranch.ballshack.setting.moduleSettings.SettingSlider;
import org.ranch.ballshack.setting.moduleSettings.TargetsDropDown;
import org.ranch.ballshack.util.EntityUtil;
import org.ranch.ballshack.util.TextUtil;
import org.ranch.ballshack.util.rendering.DrawUtil;

public class Nametags extends Module {

	public final SettingSlider minSize = dGroup.add(new SettingSlider("MinSize", 15, 5, 50, 0.5).tooltip("(its inverted)"));
	public final TargetsDropDown targets = dGroup.add(new TargetsDropDown("Targets"));
	public final SettingMode health = dGroup.add(new SettingMode("Health", 0, "None", "Num", "Fraction"));

	public Nametags() {
		super("Nametags", ModuleCategory.RENDER, 0, "wgat is your name?");
	}

	@EventSubscribe
	public void onMainWorldRender(EventWorldRender.Entity event) {
		for (Entity e : mc.world.getEntities()) {
			if (e != mc.player) {

				EntityUtil.EntityType type = EntityUtil.getEntityType(e);

				if (!targets.selected(type)) continue;

				Vec3d diff = e.getLerpedPos(event.tickDelta).add(0, e.getHeight(), 0).subtract(event.state.cameraRenderState.pos);
				double len = diff.length();
				diff = diff.normalize().multiply(Math.min(len, minSize.getValue()));

				Text tagT = Text.empty().append(e.getName().copy().withColor(type.getColor().hashCode()));
				if (health.getValue() > 0 && e instanceof LivingEntity le) {
					tagT = tagT.copy().append(Text.literal(" " + TextUtil.formatDecimal(le.getHealth(), "#.#")).withColor(EntityUtil.getHPCol(le).hashCode()));
				}
				if (health.getValue() == 2 && e instanceof LivingEntity le) { // bradar vat is this??
					tagT = tagT.copy().append(Text.literal("/").formatted(Formatting.WHITE)).append(TextUtil.formatDecimal(le.getMaxHealth(), "#.#")).withColor(Colors.DEFAULT_GREEN.hashCode());
				}

				DrawUtil.renderText3D(tagT.copy(), diff, event.matrixStack, event.state.cameraRenderState, event.queue, 1);
			}
		}
	}
}