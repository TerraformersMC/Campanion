package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.block.BaseTentBlock;
import com.terraformersmc.campanion.entity.GrapplingHookEntity;
import com.terraformersmc.campanion.entity.GrapplingHookUser;
import com.terraformersmc.campanion.entity.SleepNoSetSpawnPlayer;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.item.TentBagItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.Callable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements SleepNoSetSpawnPlayer, GrapplingHookUser {

	public GrapplingHookEntity campanion_grapplingHook;

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}

	@Shadow
	private int sleepTimer;

	@Override
	public void sleepWithoutSpawnPoint(BlockPos pos) {
		this.resetStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST));
		super.sleep(pos);
		this.sleepTimer = 0;
		if (this.world instanceof ServerWorld) {
			((ServerWorld) this.world).updateSleepingPlayers();
		}
	}

	@Inject(method = "isBlockBreakingRestricted(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/GameMode;)Z", at = @At("HEAD"), cancellable = true)
	public void isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> info) {
		if(world.getBlockState(pos).getBlock() instanceof BaseTentBlock) {
			int slotIndex = -1;
			PlayerEntity player = (PlayerEntity) (Object) this;
			for (int i = 0; i < player.inventory.size(); i++) {
				ItemStack stack = player.inventory.getStack(i);
				if (stack.getItem() == CampanionItems.TENT_BAG && !TentBagItem.hasBlocks(stack)) {
					slotIndex = i;
				}
			}
			if(!gameMode.isCreative() && slotIndex == -1) {
				info.setReturnValue(true);
			}
		}
	}

	@Shadow
	public void resetStat(Stat<?> stat) {
	}

	@Override
	public GrapplingHookEntity getGrapplingHook() {
		return campanion_grapplingHook;
	}

	@Override
	public void setGrapplingHook(GrapplingHookEntity hook) {
		campanion_grapplingHook = hook;
	}
}
