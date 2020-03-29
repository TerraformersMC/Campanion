package com.campanion.item;

import com.campanion.entity.SpearEntity;
import com.campanion.sound.CampanionSoundEvents;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.TridentItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class SpearItem extends TridentItem {

	private final ToolMaterial material;
	private final float attackDamage;
	private final float attackSpeed;
	private final Supplier<EntityType<SpearEntity>> typeSupplier;
	private EntityType<SpearEntity> cachedType = null;

	public SpearItem(ToolMaterial material, float attackDamage, float attackSpeed, Supplier<EntityType<SpearEntity>> typeSupplier, Item.Settings settings) {
		super(settings.maxDamageIfAbsent(material.getDurability()));
		this.material = material;
		this.attackSpeed = attackSpeed;
		this.attackDamage = attackDamage + material.getAttackDamage();
		this.typeSupplier = typeSupplier;
	}

	public EntityType<SpearEntity> getType() {
		if (cachedType == null) {
			cachedType = typeSupplier.get();
		}
		return cachedType;
	}

	public ToolMaterial getMaterial() {
		return this.material;
	}

	@Override
	public int getEnchantability() {
		return this.material.getEnchantability();
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return this.material.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
	}

	public float getAttackDamage() {
		return this.attackDamage;
	}

	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
		return !miner.isCreative();
	}

	@Override
	public float getMiningSpeed(ItemStack stack, BlockState state) {
		Block block = state.getBlock();
		if (block == Blocks.COBWEB) {
			return 15.0F;
		} else {
			Material material = state.getMaterial();
			return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.UNUSED_PLANT && !state.matches(BlockTags.LEAVES) && material != Material.PUMPKIN ? 1.0F : 1.5F;
		}
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damage(1, attacker, entity -> entity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (state.getHardness(world, pos) != 0.0F) {
			stack.damage(2, miner, entity -> entity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		}

		return true;
	}

	@Override
	public Multimap<String, EntityAttributeModifier> getModifiers(EquipmentSlot slot) {
		Multimap<String, EntityAttributeModifier> multimap = HashMultimap.create();
		if (slot == EquipmentSlot.MAINHAND) {
			multimap.put(EntityAttributes.ATTACK_DAMAGE.getId(), new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_UUID, "Weapon modifier", this.attackDamage, EntityAttributeModifier.Operation.ADDITION));
			multimap.put(EntityAttributes.ATTACK_SPEED.getId(), new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_UUID, "Weapon modifier", this.attackSpeed, EntityAttributeModifier.Operation.ADDITION));
		}

		return multimap;
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (user instanceof PlayerEntity) {
			PlayerEntity playerEntity = (PlayerEntity) user;
			int i = this.getMaxUseTime(stack) - remainingUseTicks;
			if (i >= 10) {
				if (!world.isClient) {
					stack.damage(1, playerEntity, entity -> entity.sendToolBreakStatus(user.getActiveHand()));
					SpearEntity spearEntity = new SpearEntity(world, playerEntity, this, stack);
					spearEntity.setProperties(playerEntity, playerEntity.pitch, playerEntity.yaw, 0.0F, 2.5F, 1.0F);
					if (playerEntity.abilities.creativeMode) {
						spearEntity.pickupType = ProjectileEntity.PickupPermission.CREATIVE_ONLY;
					}

					world.spawnEntity(spearEntity);
					world.playSoundFromEntity(null, spearEntity, CampanionSoundEvents.SPEAR_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
					if (!playerEntity.abilities.creativeMode) {
						playerEntity.inventory.removeOne(stack);
					}
				}

				playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			}
		}
	}
}
