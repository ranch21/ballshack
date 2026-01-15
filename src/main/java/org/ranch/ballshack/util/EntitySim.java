package org.ranch.ballshack.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.recipe.NetworkRecipeId;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.ranch.ballshack.BallsHack;
import org.ranch.ballshack.mixin.InputAccessor;

public class EntitySim {

    private static MinecraftClient mc = BallsHack.mc;

    private static class FakeInput extends KeyboardInput {

        private Input copiedInput;

        public FakeInput(Input origInput) {
            super(null); //LOLZ

            boolean forward = origInput.playerInput.forward();
            boolean backward = origInput.playerInput.backward();
            boolean left = origInput.playerInput.left();
            boolean right = origInput.playerInput.right();
            boolean jump = origInput.playerInput.jump();
            boolean sneak = origInput.playerInput.sneak();
            boolean sprint = origInput.playerInput.sprint();
            // NOT AGAIIINNNNNNWNDBWA VGHFEJHNKJ
            copiedInput = new Input();
            ((InputAccessor) copiedInput).setMovementVector(((InputAccessor) origInput).getMovementVector());
            copiedInput.playerInput = new PlayerInput(forward,backward,left,right,jump,sneak,sprint);
        }

        @Override
        public void tick() {
            // goot
        }
    }


    private static class FakePlayer extends ClientPlayerEntity {

        public FakePlayer(ClientWorld world, ClientPlayerEntity original) {
            super(mc, world, original.networkHandler, original.getStatHandler(), original.getRecipeBook(), original.input.playerInput, original.isSprinting());
            copyPositionAndRotation(original);
            setVelocity(original.getVelocity());
            setSprinting(original.isSprinting());
            setSneaking(original.isSneaking());
            setOnGround(original.isOnGround());
            input = new FakeInput(original.input);
        }

        @Override
        public @Nullable GameMode getGameMode() {
            return null;
        }

        @Override
        public void tick() {
            this.tickLoaded();
            if (this.isLoaded()) {
                this.getItemDropCooldown().tick();
                this.getState().tick(this.getEntityPos(), this.getVelocity());
                this.noClip = this.isSpectator();
                if (this.isSpectator() || this.hasVehicle()) {
                    this.setOnGround(false);
                }

                this.updateWaterSubmersionState();

                this.baseTick();

                if (!this.isRemoved()) {
                    this.tickMovement();
                }

                double d = this.getX() - this.lastX;
                double e = this.getZ() - this.lastZ;
                float f = (float)(d * d + e * e);
                float g = this.bodyYaw;
                if (f > 0.0025000002F) {
                    float h = (float)MathHelper.atan2(e, d) * (180.0F / (float)Math.PI) - 90.0F;
                    float k = MathHelper.abs(MathHelper.wrapDegrees(this.getYaw()) - h);
                    if (95.0F < k && k < 265.0F) {
                        g = h - 180.0F;
                    } else {
                        g = h;
                    }
                }

                if (this.handSwingProgress > 0.0F) {
                    g = this.getYaw();
                }

                Profiler profiler = Profilers.get();
                profiler.push("headTurn");
                this.turnHead(g);
                profiler.pop();
                profiler.push("rangeChecks");

                profiler.pop();
                if (this.isGliding()) {
                    this.glidingTicks++;
                } else {
                    this.glidingTicks = 0;
                }

                this.elytraFlightController.update();
                double d2 = MathHelper.clamp(this.getX(), -2.9999999E7, 2.9999999E7);
                double e2 = MathHelper.clamp(this.getZ(), -2.9999999E7, 2.9999999E7);
                if (d2 != this.getX() || e2 != this.getZ()) {
                    this.setPosition(d2, this.getY(), e2);
                }

                this.lastAttackedTicks++;

                this.updatePose();
            }
        }

        @Override
        public boolean dropSelectedItem(boolean entireStack) {
            return true;
        }

        @Override
        public void swingHand(Hand hand) {
            super.swingHand(hand);
        }

        @Override
        public void requestRespawn() {
            KeyBinding.untoggleStickyKeys();
        }

        @Override
        public void closeHandledScreen() {
            this.closeScreen();
        }

        @Override
        public void sendAbilitiesUpdate() {}

        @Override
        protected void startRidingJump() {}

        @Override
        public void openRidingInventory() {}

        @Override
        public void onRecipeDisplayed(NetworkRecipeId recipeId) {}

        @Override
        public void openDialog(RegistryEntry<Dialog> dialog) {}

        @Override
        public boolean checkGliding() {
            return false;
        }
    }

    public static void simulatePlayer(ClientPlayerEntity player) {
        FakePlayer fakePlayer = new FakePlayer(mc.world, player);

        // TODO FINISH THIS SHIT LAZY MOTHERFUCKER
    }
}
