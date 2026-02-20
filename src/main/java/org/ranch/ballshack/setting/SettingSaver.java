package org.ranch.ballshack.setting;

import com.google.gson.*;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.Constants;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleAnchor;
import org.ranch.ballshack.module.ModuleHud;
import org.ranch.ballshack.module.ModuleManager;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SettingSaver {

	private static ScheduledExecutorService scheduler;

	public static final AtomicBoolean SCHEDULE_SAVE = new AtomicBoolean();

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static Path saveDir;
}
