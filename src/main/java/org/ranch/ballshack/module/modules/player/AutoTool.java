package org.ranch.ballshack.module.modules.player;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameMode;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventPacket;
import org.ranch.ballshack.event.events.EventTick;
import org.ranch.ballshack.mixin.ClientPlayerInteractionManagerAccessor;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;
import org.ranch.ballshack.util.InvUtil;

public class AutoTool extends Module {
	public AutoTool() {
		super("AutoTool", ModuleCategory.PLAYER, 0, "Selects tool for you in an automatic fashion");
	}

	public final SettingToggle goBack = dGroup.add(new SettingToggle("Return", true));

	private int originalSlot = -1;
	private boolean switched = false;
	private boolean mining = false;

	@EventSubscribe
	public void onPacket(EventPacket.Send event) {
		if (!(event.packet instanceof PlayerActionC2SPacket)) {
			return;
		}

		PlayerActionC2SPacket packet = ((PlayerActionC2SPacket) event.packet);

		if (packet.getAction() == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
			mining = true;
		} else if (packet.getAction() == PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK || packet.getAction() == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) {
			mining = false;
		}
	}

	@EventSubscribe
	public void onTick(EventTick event) { // ouch

		if (mc.interactionManager.getCurrentGameMode() == GameMode.CREATIVE)
			return;

		HitResult crosshair = mc.crosshairTarget;
		boolean canMine = ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).getBlockBreakingCooldown() <= 0;

		if (mining && mc.options.attackKey.isPressed() && crosshair.getType() == HitResult.Type.BLOCK) {
			BlockState block = mc.world.getBlockState(((BlockHitResult) crosshair).getBlockPos());

			if (!switched)
				originalSlot = mc.player.getInventory().getSelectedSlot();

			int best = getBestSlot(block);

			if (best == mc.player.getInventory().getSelectedSlot() || !canMine)
				return;

			if (best < 9) {
				InvUtil.selectSlot(best);
			}
		} else {
			if (originalSlot != -1 && !mc.options.attackKey.isPressed()) {
				if (goBack.getValue()) {
					InvUtil.selectSlot(originalSlot);
				}
				originalSlot = -1;
			}
		}
		if (crosshair.getType() == HitResult.Type.BLOCK) {
			BlockState block = mc.world.getBlockState(((BlockHitResult) crosshair).getBlockPos());
			switched = getBestSlot(block) == mc.player.getInventory().getSelectedSlot();
		}
	}

	private int getBestSlot(BlockState block) {
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		int best = -1;
		int nonTool = -1;

		for (int i = 0; i < mc.player.getInventory().size(); i++) {
			ItemStack stack = mc.player.getInventory().getStack(i);
			if (stack == null || stack == ItemStack.EMPTY) continue;
			Item item = stack.getItem();
			double speed = item.getMiningSpeed(stack, block);
			min = Math.min(speed, min);
			if (speed > max) {
				max = item.getMiningSpeed(stack, block);
				best = i;
			}
			if (!stack.isDamageable() && nonTool == -1) {
				nonTool = i;
			}
		}

		if (min == max) {
			best = nonTool;
		}

		return best;
	}
}
