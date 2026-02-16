package org.ranch.ballshack.gui.scanner;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.network.*;
import net.minecraft.client.option.ServerList;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.ranch.ballshack.mixin.MultiplayerScreenAccessor;
import org.ranch.ballshack.mixin.MultiplayerScreenInvoker;
import org.ranch.ballshack.util.DatabaseFetcher;
import org.slf4j.Logger;
import java.awt.*;

public class ServerScannerScreen extends Screen {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final int field_41850 = 100;
	private static final int field_41851 = 74;
	private final ThreePartsLayoutWidget field_62178 = new ThreePartsLayoutWidget(this, 110, 0);
	private final MultiplayerServerListPinger serverListPinger = new MultiplayerServerListPinger();
	private final Screen parent;
	protected ServerScannerListWidget serverListWidget;
	private ServerScannerList serverList;
	private ButtonWidget buttonJoin;

	private TextFieldWidget clauseField;
	private TextFieldWidget minPlayerField;
	private TextFieldWidget maxPlayerField;
	private FilterCheckboxWidget canjoinCheckbox;
	private FilterCheckboxWidget moddedCheckbox;
	private ButtonWidget buttonSave;

	private TextWidget serverCount;

	public ServerScannerScreen(Screen parent) {
		super(Text.translatable("multiplayer.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		serverList = new ServerScannerList(this.client);
		this.serverListWidget = this.field_62178.addBody(new ServerScannerListWidget(this, this.client, this.width, this.field_62178.getContentHeight(), this.field_62178.getHeaderHeight(), client.textRenderer.fontHeight * 2 + 5));
		this.serverListWidget.setServers(this.serverList);

		DirectionalLayoutWidget verticalLayoutWidget = this.field_62178.addHeader(DirectionalLayoutWidget.vertical().spacing(4));
		verticalLayoutWidget.getMainPositioner().alignHorizontalCenter();

		verticalLayoutWidget.add(new TextWidget(Text.literal("Server Scanner Database"), this.textRenderer));

		DirectionalLayoutWidget boolFilters = verticalLayoutWidget.add(DirectionalLayoutWidget.horizontal().spacing(4));
		canjoinCheckbox = boolFilters.add(FilterCheckboxWidget.builder(
				Text.literal("Joinable"),
				textRenderer
		).build());
		moddedCheckbox = boolFilters.add(FilterCheckboxWidget.builder(
				Text.literal("Modded"),
				textRenderer
		).build());

		DirectionalLayoutWidget playerFilters = verticalLayoutWidget.add(DirectionalLayoutWidget.horizontal().spacing(4));
		playerFilters.getMainPositioner().alignHorizontalCenter();
		playerFilters.add(new TextWidget(Text.literal("MinPlayers").withColor(-2039584), this.textRenderer));
		minPlayerField = playerFilters.add(new TextFieldWidget(
				textRenderer,
				40,
				20,
				Text.literal("")
		));
		minPlayerField.setMaxLength(4);
		playerFilters.add(new TextWidget(Text.literal("MaxPlayers").withColor(-2039584), this.textRenderer));
		maxPlayerField = playerFilters.add(new TextFieldWidget(
				textRenderer,
				40,
				20,
				Text.literal("")
		));
		maxPlayerField.setMaxLength(4);

		DirectionalLayoutWidget advancedFilter = verticalLayoutWidget.add(DirectionalLayoutWidget.horizontal().spacing(4));
		advancedFilter.add(new TextWidget(Text.literal("SQL clause").withColor(-2039584), this.textRenderer));
		this.clauseField = advancedFilter.add(new TextFieldWidget(
				textRenderer,
				10,
				10,
				200,
				20,
				Text.literal("")
		));
		clauseField.setMaxLength(Integer.MAX_VALUE);

		DirectionalLayoutWidget actionButtons = verticalLayoutWidget.add(DirectionalLayoutWidget.horizontal().spacing(4));
		this.buttonJoin = actionButtons.add(ButtonWidget.builder(Text.translatable("selectServer.select"), button -> {
			ServerScannerListWidget.Entry entry = this.serverListWidget.getSelectedOrNull();
			if (entry != null) {
				entry.connect();
			}
		}).width(100).build());
		this.buttonSave = actionButtons.add(ButtonWidget.builder(Text.literal("Save"), button -> {
			ServerScannerListWidget.Entry entry = this.serverListWidget.getSelectedOrNull();
			if (entry != null && entry instanceof ServerScannerListWidget.ServerEntry serverEntry) {
				MultiplayerScreen screen = new MultiplayerScreen(new TitleScreen());
				((MultiplayerScreenAccessor) screen).setSelectedEntry(serverEntry.getServer());
				client.setScreen(screen);
				client.setScreen(new AddServerScreen(screen, Text.literal("Save Server"), (confirmed) -> ((MultiplayerScreenInvoker)screen).ballshack$invokeAddEntry(confirmed), serverEntry.getServer()));
			}
		}).width(100).build());
		actionButtons.add(ButtonWidget.builder(Text.translatable("selectServer.refresh"), button -> this.refresh()).width(74).build());
		actionButtons.add(ButtonWidget.builder(ScreenTexts.BACK, button -> this.close()).width(74).build());

		addDrawableChild(clauseField);
		this.field_62178.forEachChild(element -> {
			ClickableWidget cfr_ignored_0 = (ClickableWidget)this.addDrawableChild(element);
		});

		serverCount = addDrawableChild(new TextWidget(Text.literal(""), this.textRenderer));
		serverCount.setPosition(2, 2);

		refresh();
		this.refreshWidgetPositions();
		this.updateButtonActivationStates();
	}

	@Override
	protected void refreshWidgetPositions() {
		this.field_62178.refreshPositions();
		if (this.serverListWidget != null) {
			this.serverListWidget.position(this.width, this.field_62178);
		}
	}

	@Override
	public void close() {
		this.client.setScreen(this.parent);
	}

	@Override
	public void tick() {
		super.tick();
		this.serverListPinger.tick();
	}

	@Override
	public void removed() {
		this.serverListPinger.cancel();
		this.serverListWidget.onRemoved();
	}

	private void refresh() {
		if (clauseField.getText().isEmpty()) {
			try {
				serverList.setServerFilters(
						new DatabaseFetcher.ServerFilters()
								.canjoin(canjoinCheckbox.isChecked())
								.modded(moddedCheckbox.isChecked())
								.minPlayers(minPlayerField.getText().isEmpty() ? 0 : Integer.parseInt(minPlayerField.getText()))
								.maxPlayers(maxPlayerField.getText().isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxPlayerField.getText()))
				);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			serverList.setServerFilters(new DatabaseFetcher.ServerFilters().setClause(clauseField.getText()));
		}
		serverList.loadFile();
		serverCount.setMessage(Text.literal(serverList.size() + " loaded servers"));
		this.serverListWidget.setServers(serverList);
	}

	@Override
	public boolean keyPressed(KeyInput input) {
		if (super.keyPressed(input)) {
			return true;
		}
		if (input.key() == InputUtil.GLFW_KEY_F5) {
			this.refresh();
			return true;
		}
		return false;
	}

	public void connect(ServerInfo entry) {
		ConnectScreen.connect(this, this.client, ServerAddress.parse(entry.address), entry, false, null);
	}

	protected void updateButtonActivationStates() {
		this.buttonJoin.active = false;
		ServerScannerListWidget.Entry entry = this.serverListWidget.getSelectedOrNull();
		if (entry != null) {
			this.buttonJoin.active = true;
		}
	}

	public MultiplayerServerListPinger getServerListPinger() {
		return this.serverListPinger;
	}

	public ServerList getServerList() {
		return this.serverList;
	}
}

