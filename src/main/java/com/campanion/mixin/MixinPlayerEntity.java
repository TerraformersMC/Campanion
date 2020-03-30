package com.campanion.mixin;

import com.campanion.entity.SleepNoSetSpawnPlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements SleepNoSetSpawnPlayer {

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Shadow
	private int sleepTimer;

	@Override
	public void sleepWithSpawnPoint(BlockPos pos) {
		this.resetStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST));
		super.sleep(pos);
		this.sleepTimer = 0;
		if (this.world instanceof ServerWorld) {
			((ServerWorld) this.world).updatePlayersSleeping();
		}
	}

	@Shadow
	public void resetStat(Stat<?> stat) {
	}
}
