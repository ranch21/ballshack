package org.ranch.ballshack.event.events;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import org.ranch.ballshack.event.Event;

public class EventBlockShape extends Event {
	private BlockState state;
	private BlockPos pos;
	private VoxelShape shape;

	public EventBlockShape(BlockState blockState, BlockPos pos, VoxelShape shape) {
		this.state = blockState;
		this.pos = pos;
		this.shape = shape;
	}

	public BlockState getState() {
		return state;
	}

	public void setState(BlockState blockState) {
		this.state = blockState;
	}

	public BlockPos getPos() {
		return pos;
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
	}

	public VoxelShape getShape() {
		return shape;
	}

	public void setShape(VoxelShape shape) {
		this.shape = shape;
	}
}
