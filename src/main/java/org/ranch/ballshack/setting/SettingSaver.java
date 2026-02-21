package org.ranch.ballshack.setting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.Path;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class SettingSaver {

	private static ScheduledExecutorService scheduler;

	public static final AtomicBoolean SCHEDULE_SAVE = new AtomicBoolean();

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static Path saveDir;
}
