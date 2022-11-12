package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import com.terraformersmc.campanion.block.BaseTentBlock;
import com.terraformersmc.campanion.entity.GrapplingHookEntity;
import com.terraformersmc.campanion.entity.GrapplingHookUser;
import com.terraformersmc.campanion.entity.SleepNoSetSpawnPlayer;
import com.terraformersmc.campanion.item.BackpackItem;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.item.TentBagItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.sql.SQLSyntaxErrorException;
import java.util.concurrent.Callable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class MixinPlayerEntity extends LivingEntity implements SleepNoSetSpawnPlayer, GrapplingHookUser, BackpackStorePlayer {

	public GrapplingHookEntity campanion_grapplingHook;
	private NonNullList<ItemStack> backpackStacks = NonNullList.create();

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, Level world) {
		super(type, world);
	}

	@Shadow
	private int sleepCounter;

	@Override
	public void sleepWithoutSpawnPoint(BlockPos pos) {
		this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
		super.startSleeping(pos);
		this.sleepCounter = 0;
		if (this.level instanceof ServerLevel) {
			((ServerLevel) this.level).updateSleepingPlayerList();
		}
	}

	@Override
	public NonNullList<ItemStack> getBackpackStacks() {
		return this.backpackStacks;
	}

	@Override
	public void setBackpackStacks(NonNullList<ItemStack> stacks) {
		this.backpackStacks = stacks;
	}

	@Inject(method = "blockActionRestricted(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/GameType;)Z", at = @At("HEAD"), cancellable = true)
	public void blockActionRestricted(Level world, BlockPos pos, GameType gameMode, CallbackInfoReturnable<Boolean> info) {
		if(world.getBlockState(pos).getBlock() instanceof BaseTentBlock) {
			int slotIndex = -1;
			Player player = (Player) (Object) this;
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack stack = player.getInventory().getItem(i);
				if (stack.getItem() == CampanionItems.TENT_BAG && TentBagItem.isEmpty(stack)) {
					slotIndex = i;
				}
			}
			if(!gameMode.isCreative() && slotIndex == -1) {
				info.setReturnValue(true);
			}
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void addAdditionalSaveData(CompoundTag tag, CallbackInfo info) {
		tag.put("_campanion_backpack", ContainerHelper.saveAllItems(new CompoundTag(), this.backpackStacks));
		tag.putInt("_campanion_backpack_size", this.backpackStacks.size());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	public void readAdditionalSaveData(CompoundTag tag, CallbackInfo info) {
		this.backpackStacks.clear();
		for (int i = 0; i < tag.getInt("_campanion_backpack_size"); i++) {
			this.backpackStacks.add(ItemStack.EMPTY);
		}
		ContainerHelper.loadAllItems(tag.getCompound("_campanion_backpack"), this.backpackStacks);

		//If there are stacks in the old format, then put them in the new format
		ItemStack stack = this.getItemBySlot(EquipmentSlot.CHEST);
		if(stack.getItem() instanceof BackpackItem && stack.getOrCreateTag().contains("Inventory", 10)) {
			ContainerHelper.loadAllItems(stack.getOrCreateTag().getCompound("Inventory"), this.backpackStacks);
			stack.getTag().remove("Inventory");
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
