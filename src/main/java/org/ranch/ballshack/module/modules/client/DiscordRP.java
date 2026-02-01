package org.ranch.ballshack.module.modules.client;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventScreen;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.*;

import java.time.Instant;
import java.util.Arrays;

import static org.ranch.ballshack.util.TextUtil.applyFormatting;


public class DiscordRP extends Module {
	private static Core core;
	private static Activity activity;

	public final SettingMode activityType = dGroup.add(new SettingMode("Type", 0, Arrays.asList(
			"Playing",
			"Streaming",
			"Listening",
			"Watching",
			"Competing"
	)));
	public final SettingString details = dGroup.add(new SettingString("Details", "funny name i know"));
	public final SettingString state = dGroup.add(new SettingString("State", "yay"));

	public final DropDown partyDD = dGroup.add(new DropDown("Party"));
	public final SettingToggle pEnabled = partyDD.add(new SettingToggle("Enabled", true));
	public final SettingToggle pUsePCount = partyDD.add(new SettingToggle("UsePCount", false));
	public final SettingSlider pCurrent = partyDD.add(new SettingSlider("Current", 1, 0, 100, 1));
	public final SettingSlider pMax = partyDD.add(new SettingSlider("Max", 2, 1, 100, 1));

	public final DropDown assetsDD = dGroup.add(new DropDown("Assets"));
	public final SettingString aLarge = assetsDD.add(new SettingString("Large", "icon").maxLen(10));
	public final SettingString aSmall = assetsDD.add(new SettingString("Small", "me").maxLen(10));

	public DiscordRP() {
		super("DiscordRP", ModuleCategory.CLIENT, 0, "answer my call at ONCE kitten!!");
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.world == null)
			return;

		int updateSpeed = 20 * 4;
		if (mc.world.getTime() % updateSpeed != 0) return;
		try {
			core.runCallbacks();
			setActivity();
			core.activityManager().updateActivity(activity);
		} catch (Exception ignored) {

		}
	}

	@EventSubscribe
	public void onScreenInit(EventScreen.Init event) {
		if (mc.world != null) return;

		String screenName = event.screen.getTitle().getString();

		setActivity();
		activity.setDetails(screenName);
		activity.setState("test");
		core.activityManager().updateActivity(activity);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		// Set parameters for the Core
		try (CreateParams params = new CreateParams()) {
			params.setClientID(1403578974352707675L);
			params.setFlags(CreateParams.getDefaultFlags());
			// Create the Core
			try {
				DiscordRP.core = new Core(params);
				activity = new Activity();
				setActivity();
				activity.timestamps().setStart(Instant.now());
				core.activityManager().updateActivity(activity);
			} catch (Exception e) {
				BallsLogger.error("Discord not open");
				super.onDisable();
			}
		}
	}

	private void setActivity() {
		if (activity == null || core == null) return;
		String detailsRaw = details.getValue();
		String stateRaw = state.getValue();
		String details = applyFormatting(detailsRaw == null ? "" : detailsRaw);
		String state = applyFormatting(stateRaw == null ? "" : stateRaw);
		activity.setDetails(details);
		activity.setState(state);
		ActivityType type = ActivityType.values()[activityType.getValue()];
		activity.setType(type == ActivityType.CUSTOM ? ActivityType.COMPETING : type);
		boolean party = pEnabled.getValue();
		boolean useServerPlayerCount = pUsePCount.getValue();
		int current = (int) (double) pCurrent.getValue();
		int max = (int) (double) pMax.getValue();

		if (party) {
			if (useServerPlayerCount) {
				if (mc.isConnectedToLocalServer()) {
					max = 1;
					current = 1;
				} else if (mc.getNetworkHandler() != null && mc.getNetworkHandler().getServerInfo() != null && mc.getNetworkHandler().getServerInfo().players != null) {
					max = mc.getNetworkHandler().getServerInfo().players.max();
					current = mc.getNetworkHandler().getServerInfo().players.online();
				}
			}
			activity.party().size().setMaxSize(max);
			activity.party().size().setCurrentSize(current);
		}

		String large = aLarge.getValue();
		String small = aSmall.getValue();

		activity.assets().setLargeImage(large);
		activity.assets().setSmallImage(small);
	}

	@Override
	public void onDisable() {
		super.onDisable();
		if (core != null && core.isOpen()) {
			try {
				core.activityManager().clearActivity();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
