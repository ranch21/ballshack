package org.ranch.ballshack.gui.scanner;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.logging.LogUtils;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.WorldIcon;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.LoadingWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.logging.UncaughtExceptionLogger;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@Environment(value=EnvType.CLIENT)
public class ServerScannerListWidget extends AlwaysSelectedEntryListWidget<ServerScannerListWidget.Entry> {
	static final Identifier INCOMPATIBLE_TEXTURE = Identifier.ofVanilla("server_list/incompatible");
	static final Identifier UNREACHABLE_TEXTURE = Identifier.ofVanilla("server_list/unreachable");
	static final Identifier CANJOIN_TEXTURE = Identifier.ofVanilla("icon/checkmark");
	static final Identifier MODDED_TEXTURE = Identifier.ofVanilla("icon/unseen_notification");
	static final Identifier PING_1_TEXTURE = Identifier.ofVanilla("server_list/ping_1");
	static final Identifier PING_2_TEXTURE = Identifier.ofVanilla("server_list/ping_2");
	static final Identifier PING_3_TEXTURE = Identifier.ofVanilla("server_list/ping_3");
	static final Identifier PING_4_TEXTURE = Identifier.ofVanilla("server_list/ping_4");
	static final Identifier PING_5_TEXTURE = Identifier.ofVanilla("server_list/ping_5");
	static final Identifier PINGING_1_TEXTURE = Identifier.ofVanilla("server_list/pinging_1");
	static final Identifier PINGING_2_TEXTURE = Identifier.ofVanilla("server_list/pinging_2");
	static final Identifier PINGING_3_TEXTURE = Identifier.ofVanilla("server_list/pinging_3");
	static final Identifier PINGING_4_TEXTURE = Identifier.ofVanilla("server_list/pinging_4");
	static final Identifier PINGING_5_TEXTURE = Identifier.ofVanilla("server_list/pinging_5");
	static final Identifier JOIN_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("server_list/join_highlighted");
	static final Identifier JOIN_TEXTURE = Identifier.ofVanilla("server_list/join");
	static final Identifier MOVE_UP_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("server_list/move_up_highlighted");
	static final Identifier MOVE_UP_TEXTURE = Identifier.ofVanilla("server_list/move_up");
	static final Identifier MOVE_DOWN_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("server_list/move_down_highlighted");
	static final Identifier MOVE_DOWN_TEXTURE = Identifier.ofVanilla("server_list/move_down");
	static final Logger LOGGER = LogUtils.getLogger();
	static final ThreadPoolExecutor SERVER_PINGER_THREAD_POOL = new ScheduledThreadPoolExecutor(20, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).setUncaughtExceptionHandler(new UncaughtExceptionLogger(LOGGER)).build());
	static final Text LAN_SCANNING_TEXT = Text.translatable("lanServer.scanning");
	static final Text CANNOT_RESOLVE_TEXT = Text.translatable("multiplayer.status.cannot_resolve").withColor(Colors.RED);
	static final Text CANNOT_CONNECT_TEXT = Text.translatable("multiplayer.status.cannot_connect").withColor(Colors.RED);
	static final Text INCOMPATIBLE_TEXT = Text.translatable("multiplayer.status.incompatible");
	static final Text NO_CONNECTION_TEXT = Text.translatable("multiplayer.status.no_connection");
	static final Text PINGING_TEXT = Text.translatable("multiplayer.status.pinging");
	static final Text ONLINE_TEXT = Text.translatable("multiplayer.status.online");
	private final ServerScannerScreen screen;
	private final List<ServerScannerListWidget.ServerEntry> servers = Lists.newArrayList();

	public ServerScannerListWidget(ServerScannerScreen screen, MinecraftClient client, int width, int height, int top, int itemHeight) {
		super(client, width, height, top, itemHeight);
		this.screen = screen;
	}

	private void updateEntries() {
		ServerScannerListWidget.Entry entry = this.getSelectedOrNull();
		ArrayList<ServerScannerListWidget.Entry> list = new ArrayList<>(this.servers);
		this.replaceEntries(list);
		if (entry != null) {
			for (ServerScannerListWidget.Entry entry2 : list) {
				if (!entry2.isOfSameType(entry)) continue;
				this.setSelected(entry2);
				break;
			}
		}
	}

	@Override
	public void setSelected(@Nullable ServerScannerListWidget.Entry entry) {
		super.setSelected(entry);
		this.screen.updateButtonActivationStates();
	}

	public void setServers(ServerScannerList servers) {
		this.servers.clear();
		for (int i = 0; i < servers.size(); ++i) {
			this.servers.add(new ServerScannerListWidget.ServerEntry(this.screen, servers.get(i)));
		}
		this.updateEntries();
	}

	@Override
	public int getRowWidth() {
		return 305;
	}

	public void onRemoved() {
	}

	@Environment(value=EnvType.CLIENT)
	public static abstract class Entry extends AlwaysSelectedEntryListWidget.Entry<ServerScannerListWidget.Entry>
			implements AutoCloseable {
		@Override
		public void close() {
		}

		abstract boolean isOfSameType(ServerScannerListWidget.Entry var1);

		public abstract void connect();
	}

	@Environment(value=EnvType.CLIENT)
	public class ServerEntry extends ServerScannerListWidget.Entry {
		private final ServerScannerScreen screen;
		private final MinecraftClient client;
		private final ScannedServerInfo server;
		private final WorldIcon icon;
		@Nullable
		private byte[] favicon;
		@Nullable
		private List<Text> playerListSummary;
		@Nullable
		private Identifier statusIconTexture;
		@Nullable
		private Text statusTooltipText;

		protected ServerEntry(ServerScannerScreen screen, ScannedServerInfo server) {
			this.screen = screen;
			this.server = server;
			this.client = MinecraftClient.getInstance();
			this.icon = WorldIcon.forServer(this.client.getTextureManager(), server.address);
			this.update();
		}

		@Override
		public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
			byte[] bs;
			int j;
			int i;
			if (this.server.getStatus() == ScannedServerInfo.Status.INITIAL) {
				this.server.setStatus(ScannedServerInfo.Status.PINGING);
				this.server.label = ScreenTexts.EMPTY;
				this.server.playerCountLabel = ScreenTexts.EMPTY;
				SERVER_PINGER_THREAD_POOL.submit(() -> {
					try {
						this.screen.getServerListPinger().add(this.server, () -> this.client.execute(this::saveFile), () -> {
							this.server.setStatus(this.server.protocolVersion == SharedConstants.getGameVersion().protocolVersion() ? ScannedServerInfo.Status.SUCCESSFUL : ScannedServerInfo.Status.INCOMPATIBLE);
							this.client.execute(this::update);
						});
					} catch (UnknownHostException unknownHostException) {
						this.server.setStatus(ScannedServerInfo.Status.UNREACHABLE);
						this.server.label = CANNOT_RESOLVE_TEXT;
						this.client.execute(this::update);
					} catch (Exception exception) {
						this.server.setStatus(ScannedServerInfo.Status.UNREACHABLE);
						this.server.label = CANNOT_CONNECT_TEXT;
						this.client.execute(this::update);
					}
				});
			}
			// draw name
			//context.drawTextWithShadow(this.client.textRenderer, this.server.name, this.getContentX() + 32 + 3, this.getContentY() + 1, Colors.WHITE);
			// draw motd
			List<OrderedText> list = this.client.textRenderer.wrapLines(this.server.label, this.getContentWidth() - getContentHeight() - 2);
			for (i = 0; i < Math.min(list.size(), 1); ++i) {
				context.drawTextWithShadow(this.client.textRenderer, list.get(i), this.getContentX() + getContentHeight() + 3, this.getContentY() + 1 + this.client.textRenderer.fontHeight * i, Colors.WHITE);
			}

			if (server.canJoin) {
				context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, CANJOIN_TEXTURE, getContentRightEnd() - 10 - 5, this.getContentY() + client.textRenderer.fontHeight + 1, 9, 8);
				if (mouseX >= getContentRightEnd() - 10 - 5 && mouseX <= getContentRightEnd() - 10 - 5 + 10 && mouseY >= this.getContentY() + client.textRenderer.fontHeight && mouseY <= this.getContentY() + client.textRenderer.fontHeight + 10) {
					context.drawTooltip(Text.literal("Joinable"), mouseX, mouseY);
				}
			}

			if (server.modded) {
				context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, MODDED_TEXTURE, getContentRightEnd() - 10 - 5, this.getContentY() + client.textRenderer.fontHeight, 10, 10);
				if (mouseX >= getContentRightEnd() - 10 - 5 && mouseX <= getContentRightEnd() - 10 - 5 + 10 && mouseY >= this.getContentY() + client.textRenderer.fontHeight && mouseY <= this.getContentY() + client.textRenderer.fontHeight + 10) {
					context.drawTooltip(Text.literal("Modded"), mouseX, mouseY);
				}
			}

			this.draw(context, this.getContentX(), this.getContentY(), this.icon.getTextureId());
			i = ServerScannerListWidget.this.children().indexOf(this);
			if (this.server.getStatus() == ScannedServerInfo.Status.PINGING) {
				j = (int)(Util.getMeasuringTimeMs() / 100L + (long)(i * 2) & 7L);
				if (j > 4) {
					j = 8 - j;
				}
				this.statusIconTexture = switch (j) {
					default -> PINGING_1_TEXTURE;
					case 1 -> PINGING_2_TEXTURE;
					case 2 -> PINGING_3_TEXTURE;
					case 3 -> PINGING_4_TEXTURE;
					case 4 -> PINGING_5_TEXTURE;
				};
			}
			j = this.getContentRightEnd() - 10 - 5;
			if (this.statusIconTexture != null) {
				context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, this.statusIconTexture, j, this.getContentY(), 10, 8);
			}
			if (!Arrays.equals(bs = this.server.getFavicon(), this.favicon)) {
				if (this.uploadFavicon(bs)) {
					this.favicon = bs;
				} else {
					this.server.setFavicon(null);
					this.saveFile();
				}
			}
			Text text = this.server.getStatus() == ScannedServerInfo.Status.INCOMPATIBLE ? this.server.version.copy().formatted(Formatting.RED) : this.server.playerCountLabel;
			int k = this.client.textRenderer.getWidth(text);
			int plx = j - client.textRenderer.getWidth(this.server.playerCountLabel) - 5;
			int vx = j - client.textRenderer.getWidth(this.server.version) - 5;
			context.drawTextWithShadow(this.client.textRenderer, this.server.version.copy(), vx, this.getContentY() + 1, Colors.GRAY);
			context.drawTextWithShadow(this.client.textRenderer, this.server.playerCountLabel, plx, this.getContentY() + 1 + client.textRenderer.fontHeight, Colors.GRAY);
			if (this.statusTooltipText != null && mouseX >= j && mouseX <= j + 10 && mouseY >= this.getContentY() && mouseY <= this.getContentY() + 8) {
				context.drawTooltip(this.statusTooltipText, mouseX, mouseY);
			} else if (this.playerListSummary != null && mouseX >= plx && mouseX <= plx + k && mouseY >= this.getContentY() + client.textRenderer.fontHeight && mouseY <= getContentY() - 1 + client.textRenderer.fontHeight * 2) {
				context.drawTooltip(Lists.transform(this.playerListSummary, Text::asOrderedText), mouseX, mouseY);
			}
			if (this.client.options.getTouchscreen().getValue().booleanValue() || hovered) {
				context.fill(this.getContentX(), this.getContentY(), this.getContentX() + getContentHeight(), this.getContentY() + getContentHeight(), -1601138544);
				int m = mouseX - this.getContentX();
				int n = mouseY - this.getContentY();
				/*if (this.canConnect()) {
					if (m < 32 && m > 16) {
						context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, JOIN_HIGHLIGHTED_TEXTURE, this.getContentX(), this.getContentY(), 32, 32);
					} else {
						context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, JOIN_TEXTURE, this.getContentX(), this.getContentY(), 32, 32);
					}
				}
				*/
			}
		}

		private void update() {
			this.playerListSummary = null;
			switch (this.server.getStatus()) {
				case INITIAL:
				case PINGING: {
					this.statusIconTexture = PING_1_TEXTURE;
					this.statusTooltipText = PINGING_TEXT;
					break;
				}
				case INCOMPATIBLE: {
					this.statusIconTexture = INCOMPATIBLE_TEXTURE;
					this.statusTooltipText = INCOMPATIBLE_TEXT;
					this.playerListSummary = this.server.playerListSummary;
					break;
				}
				case UNREACHABLE: {
					this.statusIconTexture = UNREACHABLE_TEXTURE;
					this.statusTooltipText = NO_CONNECTION_TEXT;
					break;
				}
				case SUCCESSFUL: {
					this.statusIconTexture = this.server.ping < 150L ? PING_5_TEXTURE : (this.server.ping < 300L ? PING_4_TEXTURE : (this.server.ping < 600L ? PING_3_TEXTURE : (this.server.ping < 1000L ? PING_2_TEXTURE : PING_1_TEXTURE)));
					this.statusTooltipText = Text.translatable("multiplayer.status.ping", this.server.ping);
					this.playerListSummary = this.server.playerListSummary;
				}
			}
		}

		public void saveFile() {
			this.screen.getServerList().saveFile();
		}

		protected void draw(DrawContext context, int x, int y, Identifier textureId) {
			context.drawTexture(RenderPipelines.GUI_TEXTURED, textureId, x, y, 0.0f, 0.0f, getContentHeight(), getContentHeight(), getContentHeight(), getContentHeight());
		}

		private boolean canConnect() {
			return true;
		}

		private boolean uploadFavicon(@Nullable byte[] bytes) {
			if (bytes == null) {
				this.icon.destroy();
			} else {
				try {
					this.icon.load(NativeImage.read(bytes));
				} catch (Throwable throwable) {
					LOGGER.error("Invalid icon for server {} ({})", this.server.name, this.server.address, throwable);
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean keyPressed(KeyInput input) {
			if (input.isEnterOrSpace()) {
				this.connect();
				return true;
			}
			if (input.hasShift()) {
				ServerScannerListWidget multiplayerServerListWidget = this.screen.serverListWidget;
				int i = multiplayerServerListWidget.children().indexOf(this);
				if (i == -1) {
					return true;
				}
				if (input.isDown() && i < this.screen.getServerList().size() - 1 || input.isUp() && i > 0) {
					this.swapEntries(i, input.isDown() ? i + 1 : i - 1);
					return true;
				}
			}
			return super.keyPressed(input);
		}

		@Override
		public void connect() {
			this.screen.connect(this.server);
		}

		private void swapEntries(int i, int j) {
			this.screen.getServerList().swapEntries(i, j);
			this.screen.serverListWidget.swapEntriesOnPositions(i, j);
		}

		@Override
		public boolean mouseClicked(Click click, boolean doubled) {
			double d = click.x() - (double)this.getX();
			double e = click.y() - (double)this.getY();
			if (d <= 32.0) {
				if (d < 32.0 && d > 16.0 && this.canConnect()) {
					this.connect();
					return true;
				}
				int i = this.screen.serverListWidget.children().indexOf(this);
				if (d < 16.0 && e < 16.0 && i > 0) {
					this.swapEntries(i, i - 1);
					return true;
				}
				if (d < 16.0 && e > 16.0 && i < this.screen.getServerList().size() - 1) {
					this.swapEntries(i, i + 1);
					return true;
				}
			}
			if (doubled) {
				this.connect();
			}
			return super.mouseClicked(click, doubled);
		}

		public ScannedServerInfo getServer() {
			return this.server;
		}

		@Override
		public Text getNarration() {
			MutableText mutableText = Text.empty();
			mutableText.append(Text.translatable("narrator.select", this.server.name));
			mutableText.append(ScreenTexts.SENTENCE_SEPARATOR);
			switch (this.server.getStatus()) {
				case INCOMPATIBLE: {
					mutableText.append(INCOMPATIBLE_TEXT);
					mutableText.append(ScreenTexts.SENTENCE_SEPARATOR);
					mutableText.append(Text.translatable("multiplayer.status.version.narration", this.server.version));
					mutableText.append(ScreenTexts.SENTENCE_SEPARATOR);
					mutableText.append(Text.translatable("multiplayer.status.motd.narration", this.server.label));
					break;
				}
				case UNREACHABLE: {
					mutableText.append(NO_CONNECTION_TEXT);
					break;
				}
				case PINGING: {
					mutableText.append(PINGING_TEXT);
					break;
				}
				default: {
					mutableText.append(ONLINE_TEXT);
					mutableText.append(ScreenTexts.SENTENCE_SEPARATOR);
					mutableText.append(Text.translatable("multiplayer.status.ping.narration", this.server.ping));
					mutableText.append(ScreenTexts.SENTENCE_SEPARATOR);
					mutableText.append(Text.translatable("multiplayer.status.motd.narration", this.server.label));
					if (this.server.players == null) break;
					mutableText.append(ScreenTexts.SENTENCE_SEPARATOR);
					mutableText.append(Text.translatable("multiplayer.status.player_count.narration", this.server.players.online(), this.server.players.max()));
					mutableText.append(ScreenTexts.SENTENCE_SEPARATOR);
					mutableText.append(Texts.join(this.server.playerListSummary, Text.literal(", ")));
				}
			}
			return mutableText;
		}

		@Override
		public void close() {
			this.icon.close();
		}

		/*
		 * Enabled force condition propagation
		 * Lifted jumps to return sites
		 */
		@Override
		boolean isOfSameType(ServerScannerListWidget.Entry entry) {
			if (!(entry instanceof ServerScannerListWidget.ServerEntry)) return false;
			ServerScannerListWidget.ServerEntry serverEntry = (ServerScannerListWidget.ServerEntry)entry;
			if (serverEntry.server != this.server) return false;
			return true;
		}
	}
}


