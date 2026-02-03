package org.ranch.ballshack.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.CollisionView;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.event.events.EventBlockShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockCollisionSpliterator.class)
public class BlockCollisionSpliteratorMixin {

	@Final
	@Shadow
	private ShapeContext context;

	@Redirect(method = "computeNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/ShapeContext;getCollisionShape(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/CollisionView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/shape/VoxelShape;"))
	private VoxelShape getCollisionShape(ShapeContext instance, BlockState blockState, CollisionView collisionView, BlockPos blockPos) {
		VoxelShape shape = instance.getCollisionShape(blockState, collisionView, blockPos);
		if (context instanceof EntityShapeContext entShapeContext && entShapeContext.getEntity() instanceof ClientPlayerEntity) {
			EventBlockShape event = new EventBlockShape(blockState, blockPos, shape);
			BallsHack.eventBus.post(event);
			return event.shape;
		}
		return shape;
	}
}
