package org.ranch.ballshack.module.modules;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;

public class NoFall extends Module {
	public NoFall() {
		super("NoFall", ModuleCategory.PLAYER, GLFW.GLFW_KEY_F);
	}

	@EventSubscribe
	public void onTick(EventTick event) {
		if(mc.player.fallDistance > 2.5f) {
			mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
			BallsLogger.info(String.valueOf(mc.player.fallDistance));
		}
	}
}
