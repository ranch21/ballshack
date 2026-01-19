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
import org.ranch.ballshack.setting.ModuleSettings;
import org.ranch.ballshack.setting.moduleSettings.*;

import java.time.Instant;
import java.util.Arrays;

import static org.ranch.ballshack.util.TextUtil.applyFormatting;


public class DiscordRP extends Module {
	private static Core core;
	private static Activity activity;
	private final int updateSpeed = 20*4;

	public SettingMode activityType = dGroup.add(new SettingMode(0, "Type", Arrays.asList(
			"Playing",
			"Streaming",
			"Listening",
			"Watching",
			"Competing"
	)));
	public SettingString details = dGroup.add(new SettingString("Details", "test", 256));
	public SettingString state = dGroup.add(new SettingString("State", "test", 256));

	public DropDown partyDD = dGroup.add(new DropDown("Party"));
	public SettingToggle pEnabled = partyDD.add(new SettingToggle(true, "Enabled"));
	public SettingToggle pUsePCount = partyDD.add(new SettingToggle(false, "UsePCount"));
	public SettingSlider pCurrent = partyDD.add(new SettingSlider(1, "Current", 0, 100, 1));
	public SettingSlider pMax = partyDD.add(new SettingSlider(2, "Max", 1, 100, 1));

	public DropDown assetsDD = dGroup.add(new DropDown("Assets"));
	public SettingString aLarge = assetsDD.add(new SettingString("Large", "icon", 10));
	public SettingString aSmall = assetsDD.add(new SettingString("Small", "me", 10));

	public DiscordRP() {
		super("DiscordRP", ModuleCategory.CLIENT, 0, "answer my call at ONCE kitten!!");
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if (mc.world.getTime() % updateSpeed != 0) return;
		//if (activity.getState().length() < 2) return;
		//if (activity.getDetails().length() < 2) return;
		core.runCallbacks();
		setActivity();
		core.activityManager().updateActivity(activity);
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
			core = new Core(params);

			activity = new Activity();

			setActivity();

			activity.timestamps().setStart(Instant.now());
			// Setting a join secret and a party ID causes an "Ask to Join" button to appear
			//activity.party().setID("Party!");
			//activity.secrets().setJoinSecret("Join!");
			// Finally, update the current activity to our activity
			core.activityManager().updateActivity(activity);
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
				} else {
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
