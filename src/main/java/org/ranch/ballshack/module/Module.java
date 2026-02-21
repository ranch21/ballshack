package org.ranch.ballshack.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.setting.ModuleSettingsGroup;
import org.ranch.ballshack.setting.SettingSaver;
import org.ranch.ballshack.setting.settings.BindSetting;
import org.ranch.ballshack.setting.settings.BooleanSetting;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {

	protected final MinecraftClient mc = MinecraftClient.getInstance();
	protected final List<ModuleSettingsGroup> settings = new ArrayList<>();
	protected final ModuleSettingsGroup dGroup = new ModuleSettingsGroup("General");
	protected final ModuleSettingsGroup bGroup = new ModuleSettingsGroup("Bind");
	protected final BindSetting bindSetting = bGroup.add(new BindSetting("Bind", BindSetting.NONE));
	protected final BooleanSetting alertSetting = bGroup.add(new BooleanSetting("Notify", true));
	protected final BooleanSetting holdSetting = bGroup.add(new BooleanSetting("Hold", false));
	private final String name;
	private final ModuleCategory category;
	private Boolean subscribed;
	private final boolean isMeta;

	private final String tooltip;

	protected boolean enabled;

	public Module(String name, ModuleCategory category, int bind) {
		this(name, category, bind, null, false);
	}

	public Module(String name, ModuleCategory category, int bind, @Nullable String tooltip) {
		this(name, category, bind, tooltip, false);
	}

	public Module(String name, ModuleCategory category, int bind, @Nullable String tooltip, boolean isMeta) {
		bindSetting.setValue(bind);
		settings.add(dGroup);
		settings.add(bGroup);
		this.name = name;
		this.category = category;
		//settings.getBind().setValue(bind);
		this.tooltip = tooltip;
		this.isMeta = isMeta;
	}

	public void onEnable() {
		if (ModuleManager.printToggle) BallsLogger.info("Enabled module " + name);
		subscribed = BallsHack.eventBus.subscribe(this);
		enabled = true;
		SettingSaver.SCHEDULE_SAVE.set(true);
	}

	public void onDisable() {
		if (ModuleManager.printToggle) BallsLogger.info("Enabled module " + name);
		if (subscribed) {
			subscribed = !BallsHack.eventBus.unsubscribe(this);
		}
		enabled = false;
		SettingSaver.SCHEDULE_SAVE.set(true);
	}

	public int getBind() {
		return bindSetting.getValue();
	}

	public ModuleCategory getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public List<ModuleSettingsGroup> getSettings() {
		return settings;
	}

	public ModuleSettingsGroup addGroup(ModuleSettingsGroup group) {
		settings.add(group);
		return group;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void toggle() {
		if (enabled) {
			onDisable();
		} else {
			onEnable();
		}
		logToggle(enabled);
	}

	private void logToggle(boolean state) {
		MutableText text = Text.literal(name + " has been ")
				.append(Text.literal(state ? "enabled" : "disabled")
						.formatted(state ? Formatting.GREEN : Formatting.RED));
		BallsLogger.info(text);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isMeta() {
		return isMeta;
	}

	public boolean isSubscribed() {
		return subscribed;
	}
}
