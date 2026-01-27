package org.ranch.ballshack.event.events;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import org.ranch.ballshack.event.Event;

public class EventBlockShape extends Event {
	public final BlockState state;
	public final BlockPos pos;
	public VoxelShape shape;

	public EventBlockShape(BlockState blockState, BlockPos pos, VoxelShape shape) {
		this.state = blockState;
		this.pos = pos;
		this.shape = shape;
	}
}
