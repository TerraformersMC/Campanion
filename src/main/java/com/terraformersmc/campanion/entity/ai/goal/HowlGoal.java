package com.terraformersmc.campanion.entity.ai.goal;

import com.terraformersmc.campanion.entity.HowlingEntity;
import com.terraformersmc.campanion.sound.CampanionSoundEvents;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

import java.util.EnumSet;

public class HowlGoal extends Goal {

	private final MobEntity mob;
	private final World world;
	private int timer;

	public HowlGoal(MobEntity mob) {
		this.mob = mob;
		this.world = mob.world;
		this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
	}

	@Override
	public void start() {
		this.timer = 60;
		this.world.sendEntityStatus(this.mob, (byte)64);
		this.mob.getNavigation().stop();
		this.mob.playSound(CampanionSoundEvents.HOWL, 0.15F, this.mob.getRandom().nextFloat() + 0.5F);
	}

	@Override
	public void stop() {
		this.timer = 0;
	}

	@Override
	public boolean shouldContinue() {
		return getTimer() > 0;
	}

	public int getTimer() {
		return this.timer;
	}

	@Override
	public void tick() {
		this.timer = Math.max(0, this.timer - 1);
		if (this.timer < 1) {
			((HowlingEntity)mob).setHowling(false);
			this.world.sendEntityStatus(this.mob, (byte)0);
		}
	}

	//&& world.getMoonSize() == 1.0
	@Override
	public boolean canStart() {
		return world.getTimeOfDay() > 16000 && world.getTimeOfDay() < 21000  &&
				!mob.isBaby() && this.mob.getRandom().nextInt(250) == 0;
	}
}
