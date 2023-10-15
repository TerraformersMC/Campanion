package com.terraformersmc.campanion.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.terraformersmc.campanion.entity.SpearEntity;
import com.terraformersmc.campanion.sound.CampanionSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SpearItem extends TridentItem {

	private final Multimap<Attribute, AttributeModifier> attributeModifiers;

	private final Tier material;
	private final float attackDamage;
	private final Supplier<EntityType<SpearEntity>> typeSupplier;
	private EntityType<SpearEntity> cachedType = null;

	public SpearItem(Tier material, float attackDamage, float attackSpeed, Supplier<EntityType<SpearEntity>> typeSupplier, Item.Properties settings) {
		super(settings.defaultDurability(material.getUses()));
		this.material = material;
		this.attackDamage = attackDamage + material.getAttackDamageBonus();
		this.typeSupplier = typeSupplier;
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", attackSpeed, AttributeModifier.Operation.ADDITION));

		//TODO: add reach+attack range attribute for fabric and forge
//		builder.put(ReachEntityAttributes.REACH, new AttributeModifier("Reach", 1.5, AttributeModifier.Operation.ADDITION));
//		builder.put(ReachEntityAttributes.ATTACK_RANGE, new AttributeModifier("Attack range", 1.5, AttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	public EntityType<SpearEntity> getType() {
		if (cachedType == null) {
			cachedType = typeSupplier.get();
		}
		return cachedType;
	}

	public Tier getMaterial() {
		return this.material;
	}

	@Override
	public int getEnchantmentValue() {
		return this.material.getEnchantmentValue();
	}

	@Override
	public boolean isValidRepairItem(@NotNull ItemStack stack, @NotNull ItemStack ingredient) {
		return this.material.getRepairIngredient().test(ingredient) || super.isValidRepairItem(stack, ingredient);
	}

	public float getAttackDamage() {
		return this.attackDamage;
	}

	@Override
	public boolean canAttackBlock(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, Player miner) {
		return !miner.isCreative();
	}

	@Override
	public float getDestroySpeed(@NotNull ItemStack stack, BlockState state) {
		Block block = state.getBlock();
		if (block == Blocks.COBWEB) {
			return 15.0F;
		} else {
			Block material = state.getBlock();
			return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.MOSS && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level world, BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity miner) {
		if (state.getDestroySpeed(world, pos) != 0.0F) {
			stack.hurtAndBreak(2, miner, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		}

		return true;
	}

	@Override
	public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
		return equipmentSlot == EquipmentSlot.MAINHAND ? attributeModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
	}

	@Override
	public void releaseUsing(@NotNull ItemStack stack, @NotNull Level world, @NotNull LivingEntity user, int remainingUseTicks) {
		if (user instanceof Player playerEntity) {
			int i = this.getUseDuration(stack) - remainingUseTicks;
			if (i >= 10) {
				if (!world.isClientSide) {
					stack.hurtAndBreak(1, playerEntity, entity -> entity.broadcastBreakEvent(user.getUsedItemHand()));
					SpearEntity spearEntity = new SpearEntity(world, playerEntity, this, stack);
					spearEntity.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), 0.0F, 2.5F, 1.0F);
					if (playerEntity.getAbilities().instabuild) {
						spearEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					}

					world.addFreshEntity(spearEntity);
					world.playSound(null, spearEntity, CampanionSoundEvents.SPEAR_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
					if (!playerEntity.getAbilities().instabuild) {
						playerEntity.getInventory().removeItem(stack);
					}
				}

				playerEntity.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}
}
