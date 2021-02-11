package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import com.terraformersmc.campanion.block.BaseTentBlock;
import com.terraformersmc.campanion.entity.GrapplingHookEntity;
import com.terraformersmc.campanion.entity.GrapplingHookUser;
import com.terraformersmc.campanion.entity.SleepNoSetSpawnPlayer;
import com.terraformersmc.campanion.item.BackpackItem;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.item.TentBagItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.sql.SQLSyntaxErrorException;
import java.util.concurrent.Callable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements SleepNoSetSpawnPlayer, GrapplingHookUser, BackpackStorePlayer {

	public GrapplingHookEntity campanion_grapplingHook;
	private DefaultedList<ItemStack> backpackStacks = DefaultedList.of();

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

	@Override
	public DefaultedList<ItemStack> getBackpackStacks() {
		return this.backpackStacks;
	}

	@Override
	public void setBackpackStacks(DefaultedList<ItemStack> stacks) {
		this.backpackStacks = stacks;
	}

	@Inject(method = "isBlockBreakingRestricted(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/GameMode;)Z", at = @At("HEAD"), cancellable = true)
	public void isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> info) {
		if(world.getBlockState(pos).getBlock() instanceof BaseTentBlock) {
			int slotIndex = -1;
			PlayerEntity player = (PlayerEntity) (Object) this;
			for (int i = 0; i < player.inventory.size(); i++) {
				ItemStack stack = player.inventory.getStack(i);
				if (stack.getItem() == CampanionItems.TENT_BAG && TentBagItem.isEmpty(stack)) {
					slotIndex = i;
				}
			}
			if(!gameMode.isCreative() && slotIndex == -1) {
				info.setReturnValue(true);
			}
		}
	}

	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	public void writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
		tag.put("_campanion_backpack", Inventories.toTag(new CompoundTag(), this.backpackStacks));
		tag.putInt("_campanion_backpack_size", this.backpackStacks.size());
	}

	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	public void readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
		this.backpackStacks.clear();
		for (int i = 0; i < tag.getInt("_campanion_backpack_size"); i++) {
			this.backpackStacks.add(ItemStack.EMPTY);
		}
		Inventories.fromTag(tag.getCompound("_campanion_backpack"), this.backpackStacks);

		//If there are stacks in the old format, then put them in the new format
		ItemStack stack = this.getEquippedStack(EquipmentSlot.CHEST);
		if(stack.getItem() instanceof BackpackItem && stack.getOrCreateTag().contains("Inventory", 10)) {
			Inventories.fromTag(stack.getOrCreateTag().getCompound("Inventory"), this.backpackStacks);
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
