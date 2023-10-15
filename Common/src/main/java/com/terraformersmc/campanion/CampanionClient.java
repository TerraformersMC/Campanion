package com.terraformersmc.campanion;

import com.terraformersmc.campanion.blockentity.CampanionBlockEntities;
import com.terraformersmc.campanion.client.CampanionKeybinds;
import com.terraformersmc.campanion.client.renderer.blockentity.RopeBridgePostBlockEntityRenderer;
import com.terraformersmc.campanion.client.renderer.entity.EmptyRenderer;
import com.terraformersmc.campanion.client.renderer.entity.GrapplingHookEntityRenderer;
import com.terraformersmc.campanion.client.renderer.entity.SpearEntityRenderer;
import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.entity.GrapplingHookUser;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.item.SleepingBagItem;
import com.terraformersmc.campanion.item.TentBagItem;
import com.terraformersmc.campanion.network.S2CEntitySpawnGrapplingHookPacket;
import com.terraformersmc.campanion.network.S2CEntitySpawnGrapplingHookPacketHandler;
import com.terraformersmc.campanion.network.S2CSyncBackpackContents;
import com.terraformersmc.campanion.platform.Services;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;

public class CampanionClient {

	public static void registerClientPacketHandlers() {
		Services.NETWORK.registerClientBoundHandler(S2CSyncBackpackContents.class, S2CSyncBackpackContents::decode, S2CSyncBackpackContents::handle);
		Services.NETWORK.registerClientBoundHandler(S2CEntitySpawnGrapplingHookPacket.class, S2CEntitySpawnGrapplingHookPacket::decode, S2CEntitySpawnGrapplingHookPacketHandler::handle);
	}

	public static void registerModelPredicateProviders(TriConsumer<Item , ResourceLocation, ClampedItemPropertyFunction> registry) {
		registry.accept(CampanionItems.GRAPPLING_HOOK, new ResourceLocation(Campanion.MOD_ID, "deployed"), (stack, world, entity, seed) -> {
			if (entity instanceof Player) {
				for (InteractionHand value : InteractionHand.values()) {
					ItemStack heldStack = entity.getItemInHand(value);
					if (heldStack == stack && (((GrapplingHookUser) entity).getGrapplingHook() != null && !((GrapplingHookUser) entity).getGrapplingHook().isRemoved())) {
						return 1;
					}
				}
			}
			return 0;
		});
		registry.accept(CampanionItems.TENT_BAG, new ResourceLocation(Campanion.MOD_ID, "open"), (stack, world, entity, seed) -> TentBagItem.isEmpty(stack) ? 1 : 0);
		registry.accept(CampanionItems.SLEEPING_BAG, new ResourceLocation(Campanion.MOD_ID, "open"), (stack, world, entity, seed) -> SleepingBagItem.inUse(stack) ? 1 : 0);
	}

	public static void registerKeybindings(Consumer<KeyMapping> registery) {
		registery.accept(CampanionKeybinds.OPEN_BACKPACK_KEY);
	}

	public static void registerEntityRenderers(RegisterEntityRendersCallback registry) {
		registry.register(CampanionEntities.WOODEN_SPEAR, SpearEntityRenderer::new);
		registry.register(CampanionEntities.STONE_SPEAR, SpearEntityRenderer::new);
		registry.register(CampanionEntities.IRON_SPEAR, SpearEntityRenderer::new);
		registry.register(CampanionEntities.GOLDEN_SPEAR, SpearEntityRenderer::new);
		registry.register(CampanionEntities.DIAMOND_SPEAR, SpearEntityRenderer::new);
		registry.register(CampanionEntities.NETHERITE_SPEAR, SpearEntityRenderer::new);

		registry.register(CampanionEntities.GRAPPLING_HOOK, GrapplingHookEntityRenderer::new);
		registry.register(CampanionEntities.LAWN_CHAIR, EmptyRenderer::new);

		registry.register(CampanionEntities.THROWING_STONE, ThrownItemRenderer::new);
		registry.register(CampanionEntities.FLARE, ThrownItemRenderer::new);
	}

	public static void registerBlockEntityRenderers(RegisterBlockEntityRendersCallback registry) {
		registry.register(CampanionBlockEntities.ROPE_BRIDGE_POST, RopeBridgePostBlockEntityRenderer::new);
	}

	public static void registerItemColours(RegisterItemColoursCallback registry) {
		registry.register((stack, tintIndex) -> tintIndex == 0 ? ((DyeableLeatherItem) stack.getItem()).getColor(stack) : -1, CampanionItems.SLEEPING_BAG);
	}

	interface RegisterEntityRendersCallback {
		<E extends Entity> void register (EntityType<? extends E> entityType, EntityRendererProvider<E> entityRendererFactory);
	}

	interface RegisterBlockEntityRendersCallback {
		<E extends BlockEntity> void register(BlockEntityType<E> blockEntityType, BlockEntityRendererProvider<? super E> blockEntityRendererFactory);
	}

	interface RegisterItemColoursCallback {
		void register(ItemColor itemColor, Item... items);
	}
}
