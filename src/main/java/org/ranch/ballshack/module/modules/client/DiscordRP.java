package org.ranch.ballshack.module.modules.client;
import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

import java.nio.channels.ClosedChannelException;
import java.time.Instant;


public class DiscordRP extends Module {
    private static Core core;
    private static Activity activity;

    public DiscordRP() {
        super("DiscordRP", ModuleCategory.CLIENT, 0);
    }

    @EventSubscribe
    public void onTick(EventTick event) {
        core.runCallbacks();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        // Set parameters for the Core
        try(CreateParams params = new CreateParams())
        {
            params.setClientID(1403578974352707675L);
            params.setFlags(CreateParams.getDefaultFlags());
            // Create the Core
            core = new Core(params);

            activity = new Activity();

            activity.setDetails("balhack");
            activity.setState("hh");

                // Setting a start time causes an "elapsed" field to appear
            activity.timestamps().setStart(Instant.now());

                // We are in a party with 10 out of 100 people.
                //activity.party().size().setMaxSize(100);
                //activity.party().size().setCurrentSize(10);
                // Make a "cool" image show up
            activity.assets().setLargeImage("test");

                // Setting a join secret and a party ID causes an "Ask to Join" button to appear
                //activity.party().setID("Party!");
                //activity.secrets().setJoinSecret("Join!");
                // Finally, update the current activity to our activity
            core.activityManager().updateActivity(activity);
        }
    }

    private void updateActivity() {

    }

    @Override
    public void onDisable() {
        super.onDisable();
        BallsLogger.info(String.valueOf(core.isOpen()));
        if (core != null && core.isOpen()) {
            try {
                core.activityManager().clearActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
