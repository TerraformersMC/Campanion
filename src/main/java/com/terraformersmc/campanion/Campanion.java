package com.terraformersmc.campanion;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.terraformersmc.campanion.advancement.criterion.CampanionCriteria;
import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.blockentity.CampanionBlockEntities;
import com.terraformersmc.campanion.config.CampanionConfigManager;
import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.entity.FlareEntity;
import com.terraformersmc.campanion.entity.SkippingStoneEntity;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.network.C2SOpenBackpack;
import com.terraformersmc.campanion.network.C2SRotateHeldItem;
import com.terraformersmc.campanion.recipe.CampanionRecipeSerializers;
import com.terraformersmc.campanion.sound.CampanionSoundEvents;
import com.terraformersmc.campanion.stat.CampanionStats;
import com.terraformersmc.campanion.tag.CampanionBlockTags;
import com.terraformersmc.campanion.tag.CampanionItemTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class Campanion implements ModInitializer {

	public static final String MOD_ID = "campanion";
	public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

	@Override
	public void onInitialize() {
		register();
	}

	public static void register() {
		CampanionConfigManager.initializeConfig();

		CampanionSoundEvents.register();
		CampanionItems.register();
		CampanionBlocks.register();
		CampanionBlockEntities.register();
		CampanionEntities.register();
		CampanionRecipeSerializers.register();

		CampanionCriteria.loadClass();
		CampanionStats.loadClass();

		FabricItemGroupBuilder.create(new Identifier(MOD_ID, "items")).icon(() -> CampanionItems.SMORE.asItem().getDefaultStack()).appendItems(stacks -> Registry.ITEM.forEach(item -> {
			if (Registry.ITEM.getId(item).getNamespace().equals(MOD_ID)) {
				item.appendStacks(item.getGroup(), (DefaultedList<ItemStack>) stacks);
			}
		})).build();

		registerServerboundPackets();

		DispenserBlock.registerBehavior(CampanionItems.SKIPPING_STONE, new ProjectileDispenserBehavior() {
			@Override
			protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
				return Util.make(new SkippingStoneEntity(world, position.getX(), position.getY(), position.getZ()), (snowballEntity) -> {
					snowballEntity.setItem(stack);
				});
			}
		});

		DispenserBlock.registerBehavior(CampanionItems.FLARE, new ProjectileDispenserBehavior() {
			@Override
			protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
				return Util.make(new FlareEntity(world, position.getX(), position.getY(), position.getZ()), (flareEntity) -> {
					flareEntity.setItem(stack);
				});
			}
		});

		CampanionBlockTags.load();
		CampanionItemTags.load();
	}

	public static void registerServerboundPackets() {
		ServerPlayNetworking.registerGlobalReceiver(C2SOpenBackpack.ID, C2SOpenBackpack::onPacket);
		ServerPlayNetworking.registerGlobalReceiver(C2SRotateHeldItem.ID, C2SRotateHeldItem::onPacket);
	}
}
