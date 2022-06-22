package com.terraformersmc.campanion.entity.ai.goal;

import com.terraformersmc.campanion.entity.HowlingEntity;
import com.terraformersmc.campanion.sound.CampanionSoundEvents;
import java.util.EnumSet;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

public class HowlGoal extends Goal {

	private final Mob mob;
	private final Level world;
	private int timer;

	public HowlGoal(Mob mob) {
		this.mob = mob;
		this.world = mob.level;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
	}

	@Override
	public void start() {
		this.timer = 60;
		this.world.broadcastEntityEvent(this.mob, (byte)64);
		this.mob.getNavigation().stop();
		this.mob.playSound(CampanionSoundEvents.HOWL, 0.15F, this.mob.getRandom().nextFloat() + 0.5F);
	}

	@Override
	public void stop() {
		this.timer = 0;
	}

	@Override
	public boolean canContinueToUse() {
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
			this.world.broadcastEntityEvent(this.mob, (byte)0);
		}
	}

	//&& world.getMoonSize() == 1.0
	@Override
	public boolean canUse() {
		return world.getDayTime() > 16000 && world.getDayTime() < 21000  &&
				!mob.isBaby() && this.mob.getRandom().nextInt(250) == 0;
	}
}
