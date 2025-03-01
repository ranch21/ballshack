package org.ranch.ballshack.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.BlockView;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventBlockShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockCollisionSpliterator.class)
public class BlockCollisionSpliteratorMixin {
	@Redirect(method = "computeNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"))
	private VoxelShape computeNext_getCollisionShape(BlockState instance, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
		VoxelShape shape = instance.getCollisionShape(blockView, blockPos, shapeContext);
		EventBlockShape event = new EventBlockShape(instance, blockPos, shape);
		BallsHack.eventBus.post(event);

		return event.getShape();
	}
}
