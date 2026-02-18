package org.ranch.ballshack.gui.scanner;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.input.AbstractInput;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import org.ranch.ballshack.util.DatabaseFetcher;

import java.awt.*;
import java.util.Objects;

public class FilterCheckboxWidget extends PressableWidget {
	private static final Identifier SELECTED_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/checkbox_selected_highlighted");
	private static final Identifier SELECTED_TEXTURE = Identifier.ofVanilla("widget/checkbox_selected");
	private static final Identifier HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/checkbox_highlighted");
	private static final Identifier TEXTURE = Identifier.ofVanilla("widget/checkbox");
	private static final int TEXT_COLOR = -2039584;
	private static final int field_47105 = 4;
	private static final int field_47106 = 8;
	private DatabaseFetcher.ServerFilters.FilterBool checked;
	private final Callback callback;
	private final MultilineTextWidget textWidget;

	FilterCheckboxWidget(int x, int y, int maxWidth, Text message, TextRenderer textRenderer, DatabaseFetcher.ServerFilters.FilterBool checked, Callback callback) {
		super(x, y, 0, 0, message);
		this.width = this.calculateWidth(maxWidth, message, textRenderer);
		this.textWidget = (new MultilineTextWidget(message.copy().withColor(-2039584), textRenderer)).setMaxWidth(this.width);
		this.height = this.calculateHeight(textRenderer);
		this.checked = checked;
		this.callback = callback;
	}

	private int calculateWidth(int max, Text text, TextRenderer textRenderer) {
		return Math.min(calculateWidth(text, textRenderer), max);
	}

	private int calculateHeight(TextRenderer textRenderer) {
		return Math.max(getCheckboxSize(textRenderer), this.textWidget.getHeight());
	}

	static int calculateWidth(Text text, TextRenderer textRenderer) {
		return getCheckboxSize(textRenderer) + 4 + textRenderer.getWidth((StringVisitable) text);
	}

	public static Builder builder(Text text, TextRenderer textRenderer) {
		return new Builder(text, textRenderer);
	}

	public static int getCheckboxSize(TextRenderer textRenderer) {
		Objects.requireNonNull(textRenderer);
		return 9 + 8;
	}

	public void onPress(AbstractInput input) {
		checked = checked.cycle();
		callback.onValueChange(this, this.checked);
	}

	public DatabaseFetcher.ServerFilters.FilterBool isChecked() {
		return this.checked;
	}

	public void appendClickableNarrations(NarrationMessageBuilder builder) {
		builder.put(NarrationPart.TITLE, (Text) this.getNarrationMessage());
		if (this.active) {
			if (this.isFocused()) {
				builder.put(NarrationPart.USAGE, (Text) Text.translatable("narration.checkbox.usage.focused"));
			} else {
				builder.put(NarrationPart.USAGE, (Text) Text.translatable("narration.checkbox.usage.hovered"));
			}
		}

	}

	@Override
	protected void drawIcon(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		TextRenderer textRenderer = minecraftClient.textRenderer;
		Identifier identifier;
		if (checked == DatabaseFetcher.ServerFilters.FilterBool.TRUE) {
			identifier = this.isFocused() ? SELECTED_HIGHLIGHTED_TEXTURE : SELECTED_TEXTURE;
		} else {
			identifier = this.isFocused() ? HIGHLIGHTED_TEXTURE : TEXTURE;
		}

		int i = getCheckboxSize(textRenderer);
		context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier, this.getX(), this.getY(), i, i, ColorHelper.getWhite(this.alpha));
		if (checked == DatabaseFetcher.ServerFilters.FilterBool.ANY) {
			context.drawText(textRenderer, "A", this.getX() + i / 2 - textRenderer.getWidth("A") / 2, this.getY() + getHeight() / 2 - textRenderer.fontHeight / 2, Color.WHITE.hashCode(), true);
		}
		int j = this.getX() + i + 4;
		int k = this.getY() + i / 2 - this.textWidget.getHeight() / 2;
		this.textWidget.setPosition(j, k);
		this.textWidget.renderWidget(context, mouseX, mouseY, deltaTicks);
	}

	@Environment(EnvType.CLIENT)
	public interface Callback {
		Callback EMPTY = (checkbox, checked) -> {
		};

		void onValueChange(FilterCheckboxWidget checkbox, DatabaseFetcher.ServerFilters.FilterBool checked);
	}

	@Environment(EnvType.CLIENT)
	public static class Builder {
		private final Text message;
		private final TextRenderer textRenderer;
		private int maxWidth;
		private int x = 0;
		private int y = 0;
		private Callback callback;
		private DatabaseFetcher.ServerFilters.FilterBool checked;
		@Nullable
		private SimpleOption<DatabaseFetcher.ServerFilters.FilterBool> option;
		@Nullable
		private Tooltip tooltip;

		Builder(Text message, TextRenderer textRenderer) {
			this.callback = Callback.EMPTY;
			this.checked = DatabaseFetcher.ServerFilters.FilterBool.ANY;
			this.option = null;
			this.tooltip = null;
			this.message = message;
			this.textRenderer = textRenderer;
			this.maxWidth = calculateWidth(message, textRenderer);
		}

		public Builder pos(int x, int y) {
			this.x = x;
			this.y = y;
			return this;
		}

		public Builder callback(Callback callback) {
			this.callback = callback;
			return this;
		}

		public Builder checked(DatabaseFetcher.ServerFilters.FilterBool checked) {
			this.checked = checked;
			this.option = null;
			return this;
		}

		public Builder option(SimpleOption<DatabaseFetcher.ServerFilters.FilterBool> option) {
			this.option = option;
			this.checked = option.getValue();
			return this;
		}

		public Builder tooltip(Tooltip tooltip) {
			this.tooltip = tooltip;
			return this;
		}

		public Builder maxWidth(int maxWidth) {
			this.maxWidth = maxWidth;
			return this;
		}

		public FilterCheckboxWidget build() {
			FilterCheckboxWidget.Callback callback = this.option == null ? this.callback : (checkbox, checked) -> {
				this.option.setValue(checked);
				this.callback.onValueChange(checkbox, checked);
			};
			FilterCheckboxWidget checkboxWidget = new FilterCheckboxWidget(this.x, this.y, this.maxWidth, this.message, this.textRenderer, this.checked, callback);
			checkboxWidget.setTooltip(this.tooltip);
			return checkboxWidget;
		}
	}
}
