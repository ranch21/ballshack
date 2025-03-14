package org.ranch.ballshack.module.modules.hud;

import com.google.common.collect.Streams;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventHudRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.module.ModuleHud;
import org.ranch.ballshack.module.ModuleManager;
import org.ranch.ballshack.module.modules.client.ClickGui;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.settings.SettingMode;
import org.ranch.ballshack.setting.settings.SettingToggle;
import org.ranch.ballshack.util.DrawUtil;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class ModuleList extends ModuleHud {
	float totalTicks = 0;

	int height = 0;

	public ModuleList() {
		super("ModuleList", ModuleCategory.HUD, 0, 0, 0, new ModuleSettings(Arrays.asList(
				new SettingMode(0, "ColMode", Arrays.asList("Cat", "Rand", "Rain")),
				new SettingToggle(true, "Backdrop"),
				new SettingToggle(true, "Shadow"),
				new SettingToggle(true, "Line"),
				new SettingToggle(true, "Watermark"),
				new SettingToggle(false, "logo")
		)));
	}

	@EventSubscribe
	public void onHudRender(EventHudRender event) {

		Stream<Module> modules = Streams.stream(ModuleManager.getModules());

		Comparator<Module> comparator = Comparator.comparing(m -> {
			String name  = m.getName();
			String setting = getFeaturedSetting(m) == null ? "" : " [" + getFeaturedSetting(m) + "]";
			return mc.textRenderer.getWidth(name + setting);
		});

		modules = modules.filter(Module::isEnabled).sorted(comparator.reversed());

		int cMode = (int) getSettings().getSetting(0).getValue();
		boolean backdrop = (boolean) getSettings().getSetting(1).getValue();
		boolean shadow = (boolean) getSettings().getSetting(2).getValue();
		boolean line = (boolean) getSettings().getSetting(3).getValue();
		boolean watermark = (boolean) getSettings().getSetting(4).getValue();
		boolean logo = (boolean) getSettings().getSetting(5).getValue();

		DrawContext context = event.drawContext;
		TextRenderer textR = mc.textRenderer;
		totalTicks += event.tickDelta;

		int i = 0;

		int sX = line ? 1 : 0;

		int addedHeight = 0;

		if (logo) {
			Identifier texture = Identifier.of("ballshack", "ballshack.png");
			context.drawTexture(RenderLayer::getGuiTextured, texture, 0, 0, 0, 0, 50, 16, 50, 16);
			addedHeight += 16;
		}

		if (watermark) {
			if (backdrop) {
				context.fill(sX, y + addedHeight, sX + textR.getWidth(BallsHack.title + " " + BallsHack.version) + 3, y + textR.fontHeight + 1 + addedHeight, Colors.BACKDROP.hashCode());
			}

			if (line) {
				context.drawVerticalLine(x, y - 1, y + textR.fontHeight + 1 + addedHeight, Colors.PALLETE_1.hashCode());
			}
			DrawUtil.drawText(context, textR, BallsHack.title, sX + 2, y + 1 + addedHeight, Colors.PALLETE_1, true);
			DrawUtil.drawText(context, textR, BallsHack.version, sX + 2 + textR.getWidth(BallsHack.title + " "), y + 1 + addedHeight, Color.WHITE, true);
			i++;
		}

		Random r = new Random();

		for (Module m : modules.toList()) {

			if (m instanceof ClickGui || m instanceof ModuleHud) {
				continue;
			}

			Color col;

			switch (cMode) {
				case 0:
					col = Colors.CATEGORY_COLORS[m.getCategory().ordinal()];
					break;
				case 1:
					r.setSeed(m.getName().hashCode());
					col = Color.getHSBColor(r.nextFloat(), 0.7f, 1.0f);
					break;
				case 2:
					col = Colors.getRainbowColorGlobal(totalTicks + i * 10);
					break;
				default:
					col = Color.WHITE;
			}

			String featured = getFeaturedSetting(m);

			int featureWidth = featured == null ? 0 : textR.getWidth(" [" + featured + "]");

			if (backdrop) {
				context.fill(sX, y + i * (textR.fontHeight + 1) + addedHeight, sX + textR.getWidth(m.getName()) + 3 + featureWidth, y + textR.fontHeight + i * (textR.fontHeight + 1) + 1 + addedHeight, Colors.BACKDROP.hashCode());
			}

			if (line) {
				context.drawVerticalLine(x, y + i * (textR.fontHeight + 1) - 1 + addedHeight, y + i * (textR.fontHeight + 1) + textR.fontHeight + 1 + addedHeight, col.hashCode());
			}

			context.drawText(textR, m.getName(), sX + 2, y + 1 + i * (textR.fontHeight + 1) + addedHeight, col.hashCode(), shadow);
			if (featured != null) {
				context.drawText(textR, " [" + featured + "]", sX + 2 + textR.getWidth(m.getName()), y + 1 + i * (textR.fontHeight + 1) + addedHeight, Color.lightGray.hashCode(), shadow);
			}
			i++;
		}
		height = y + textR.fontHeight + i * (textR.fontHeight + 1) + 1 + addedHeight;
	}

	private String getFeaturedSetting(Module module) {
		for (ModuleSetting<?> m : module.getSettings().getSettings()) {
			if (m.isFeatured()) {
				return m.getFormattedValue();
			}
		}
		return null;
	}

	@Override
	public int getWidth() {
		return 50;
	}

	@Override
	public int getHeight() {
		return height;
	}
}
