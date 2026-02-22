package org.ranch.ballshack.module.modules.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventHudRender;
import org.ranch.ballshack.gui.Colors;
import org.ranch.ballshack.module.*;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.setting.ModuleSetting;
import org.ranch.ballshack.setting.ModuleSettingsGroup;
import org.ranch.ballshack.setting.settings.BooleanSetting;
import org.ranch.ballshack.setting.settings.ModeSetting;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class ModuleList extends ModuleHud {
	float totalTicks = 0;

	public static enum ColorMode {
		CATEGORY, RANDOM, RAINBOW
	}

	public final ModeSetting<ColorMode> colorMode = dGroup.add(new ModeSetting<>("ColMode", ColorMode.RANDOM, ColorMode.values()));
	public final BooleanSetting backdrop = dGroup.add(new BooleanSetting("Backdrop", true));
	public final BooleanSetting shadow = dGroup.add(new BooleanSetting("Shadow", true));
	public final BooleanSetting line = dGroup.add(new BooleanSetting("Line", true));

	public ModuleList() {
		super("ModuleList", ModuleCategory.HUD, 0, 0, 0, "Incase you forgor what you enabled", ModuleAnchor.TOP_RIGHT);
	}

	@EventSubscribe
	public void onHudRender(EventHudRender.Post event) {

		Stream<Module> modules = ModuleManager.getModules().stream();

		Comparator<Module> comparator = Comparator.comparing(m -> {
			String name = m.getName();
			String setting = getFeaturedSettings(m).isEmpty() ? "" : " [" + getFeaturedSettings(m) + "]";
			return mc.textRenderer.getWidth(name + setting);
		});

		modules = modules.filter(m -> m.isEnabled() && !m.isMeta()).sorted(comparator.reversed());
		List<Module> sortedModules = modules.toList();
		int widest = mc.textRenderer.getWidth(getModuleText(sortedModules.get(0), Color.WHITE));
		int index = 0;
		for (Module module : sortedModules) {
			Color col = getModuleColor(module, colorMode.getValue(), index);
			drawLine(event.drawContext, getModuleText(module, col), col, line.getValue(), backdrop.getValue(), shadow.getValue(), widest, index);
			index++;
		}
		height = (mc.textRenderer.fontHeight + 1) * index;
		width = widest;
		totalTicks++;
	}

	private void drawLine(DrawContext context, Text text, Color color, boolean line, boolean background, boolean shadow, int widest, int index) {
		int textPadding = 0;
		int textWidth = mc.textRenderer.getWidth(text);
		if (getAnchorPoint().isCenter()) {
			textPadding = (widest - textWidth) / 2;
		} else if (getAnchorPoint().isRight()) {
			textPadding = widest - textWidth;
		}

		int linePadding = 0;

		if (line) linePadding += getAnchorPoint().isRight() ? -2 : 2;

		int baseX = X() - xOffset() + textPadding + linePadding;
		int baseY = Y() - yOffset() + (mc.textRenderer.fontHeight + 1) * index + 1;

		if (background) {
			context.fill(baseX - 1, baseY - 1, baseX + textWidth + 1, baseY + mc.textRenderer.fontHeight, Colors.HUD_BACKGROUND.getColor().hashCode());
		}

		if (line && !getAnchorPoint().isCenter()) {
			context.drawVerticalLine(getAnchorPoint().isRight() ? baseX + textWidth + linePadding + 3 : baseX - linePadding, baseY - 2, mc.textRenderer.fontHeight + baseY, color.hashCode());
		}

		context.drawText(mc.textRenderer, text, baseX, baseY, Color.WHITE.hashCode(), shadow);
	}

	private Color getModuleColor(Module module, ColorMode cMode, int i) {
		Color col;
		Random r = new Random();
		col = switch (cMode) {
			case CATEGORY -> module.getCategory().getColor();
			case RANDOM -> {
				r.setSeed(module.getName().hashCode());
				yield Color.getHSBColor(r.nextFloat(), 0.7f, 1.0f);
			}
			case RAINBOW -> Colors.getRainbowColorGlobal(totalTicks + i * 10);
		};
		return col;
	}

	private Text getModuleText(Module module, Color color) {


		MutableText text = Text.of(module.getName()).copy().withColor(color.hashCode());

		String feat = getFeaturedSettings(module);
		if (!feat.isEmpty()) {
			text.append(Text.of(" [" + feat + "]").copy().withColor(Colors.DULL_GRAY.hashCode()));
		}
		return text;
	}

	private String getFeaturedSettings(Module module) {
		StringBuilder feat = new StringBuilder();
		for (ModuleSettingsGroup g : module.getSettings()) {
			for (ModuleSetting<?, ?> m : g.getSettings()) {
				if (m.isFeatured()) {
					if (!feat.isEmpty()) {
						feat.append(", ");
					}
					feat.append(m.getFormattedValue());
				}
			}
		}
		return feat.toString();
	}

}
