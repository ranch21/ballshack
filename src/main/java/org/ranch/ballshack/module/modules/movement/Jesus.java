package org.ranch.ballshack.module.modules.movement;

import net.minecraft.util.shape.VoxelShapes;
import org.ranch.ballshack.BallsLogger;
import org.ranch.ballshack.event.EventSubscribe;
import org.ranch.ballshack.event.events.EventBlockShape;
import org.ranch.ballshack.module.Module;
import org.ranch.ballshack.module.ModuleCategory;
import org.ranch.ballshack.setting.moduleSettings.SettingToggle;

public class Jesus extends Module {

	public final SettingToggle sides = dGroup.add((SettingToggle) new SettingToggle(true, "Sides").featured());

	public Jesus() {
		super("Jesus", ModuleCategory.MOVEMENT, 0, "Atheists cannot explain");
	}

	@EventSubscribe
	public void onBlockShape(EventBlockShape event) {
		if (mc.world == null || mc.player == null) return;
		if (!mc.world.getFluidState(event.pos).isEmpty() // event is water
				&& !mc.player.isSneaking() // not sneaking
				&& !mc.player.isTouchingWater() // not touching water
		) {
			if (sides.getValue()) {
				event.shape = VoxelShapes.cuboid(0, 0, 0, 1, 0.9, 1);
			} else if (mc.player.getY() >= event.pos.getY() + 0.9) { // above water
				event.shape = VoxelShapes.cuboid(0, 0, 0, 1, 0.9, 1);
			}
		}
	}

	private int iamsofunny = 0;

	@Override
	public void onEnable() {
		switch (iamsofunny) {
			case 0:
				BallsLogger.info("dear ballshack user i regret to inform you that this module is out of commision. :(");
				iamsofunny = 1;
				break;
			case 1:
				BallsLogger.info("i said its not working");
				iamsofunny = 2;
				break;
			case 2:
				BallsLogger.info("no worky");
				iamsofunny = 3;
				break;
			case 3:
				BallsLogger.info("停止使用 / काम से बाहर / hors service / خارج الخدمة / außer Betrieb / 故障中");
				iamsofunny = 4;
				break;
			case 4:
				BallsLogger.info("停止使用 / काम से बाहर / hors service / خارج الخدمة / außer Betrieb / 故障中");
				iamsofunny = 5;
				break;
			case 5:
				BallsLogger.info("fuck off");
				iamsofunny = 6;
				break;
			case 6:
				BallsLogger.info("dear ballshack user i regret to inform you that this module is out of commision. :(");
				iamsofunny = 7;
				break;
			case 7:
				BallsLogger.info("\"hehe look at me im so funny i keep pressing the fucking button\"");
				iamsofunny = 8;
				break;
			case 8:
				BallsLogger.info("goot");
				System.exit(-1);
				break;
		}
		//super.onEnable();
	}
}
