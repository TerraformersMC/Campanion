package com.campanion.mixin;

import com.campanion.entity.HowlingEntity;
import com.campanion.entity.ai.goal.HowlGoal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfEntity.class)
public abstract class MixinWolfEntity extends TameableEntity implements HowlingEntity {

	private static final TrackedData<Boolean> HOWLING;

	private float howlAnimationProgress;
	//For when I want to animate this
	private float lastHowlAnimationProgress;

	protected MixinWolfEntity(EntityType<? extends TameableEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	public void tick(CallbackInfo callbackInfo) {
		if (this.isAlive()) {
			this.lastHowlAnimationProgress = this.howlAnimationProgress;
			if (this.isHowling()) {
				this.howlAnimationProgress += (1.0F - this.howlAnimationProgress) * 0.4F;
			} else {
				this.howlAnimationProgress += (0.0F - this.howlAnimationProgress) * 0.4F;
			}
		}
	}

	@Inject(method = "initGoals", at = @At("HEAD"), cancellable = true)
	protected void initGoals(CallbackInfo callbackInfo) {
		this.goalSelector.add(5, new HowlGoal(this));
	}

	@Inject(method = "handleStatus", at = @At("HEAD"), cancellable = true)
	@Environment(EnvType.CLIENT)
	public void handleStatus(byte status, CallbackInfo callbackInfo) {
		if (status == 10) {
			setHowling(true);
			this.howlAnimationProgress = 0.0F;
			this.lastHowlAnimationProgress = 0.0F;
			callbackInfo.cancel();
		}
	}

	public void setHowling(boolean howling) {
		this.dataTracker.set(HOWLING, howling);
	}

	public boolean isHowling() {
		return (Boolean)this.dataTracker.get(HOWLING);
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"), cancellable = true)
	protected void initDataTracker(CallbackInfo callbackInfo) {
		this.dataTracker.startTracking(HOWLING, false);
	}

	static {
		HOWLING = DataTracker.registerData(WolfEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	}

}
