package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.entity.HowlingEntity;
import com.terraformersmc.campanion.entity.ai.goal.HowlGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Wolf.class)
public abstract class MixinWolfEntity extends TamableAnimal implements HowlingEntity {

	private static final EntityDataAccessor<Boolean> HOWLING = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.BOOLEAN);

	private float howlAnimationProgress;
	//For when I want to animate this
	private float lastHowlAnimationProgress;

	protected MixinWolfEntity(EntityType<? extends TamableAnimal> type, Level world) {
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

	@Inject(method = "registerGoals", at = @At("HEAD"), cancellable = true)
	protected void registerGoals(CallbackInfo callbackInfo) {
		this.goalSelector.addGoal(5, new HowlGoal(this));
	}

	@Override
	public void setHowling(boolean howling) {
		if(howling) {
			this.howlAnimationProgress = 0.0F;
			this.lastHowlAnimationProgress = 0.0F;
		}
		this.entityData.set(HOWLING, howling);
	}

	@Override
	public boolean isHowling() {
		return this.entityData.get(HOWLING);
	}

	@Override
	public float getHowlAnimationProgress(float delta) {
		return this.lastHowlAnimationProgress + (this.howlAnimationProgress - this.lastHowlAnimationProgress) * delta;
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"), cancellable = true)
	protected void defineSynchedData(CallbackInfo callbackInfo) {
		this.entityData.define(HOWLING, false);
	}


}
