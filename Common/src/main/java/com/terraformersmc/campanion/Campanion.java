package com.terraformersmc.campanion;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.terraformersmc.campanion.advancement.criterion.CampanionCriteria;
import com.terraformersmc.campanion.config.CampanionConfigManager;
import com.terraformersmc.campanion.entity.FlareEntity;
import com.terraformersmc.campanion.entity.SkippingStoneEntity;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.network.C2SOpenBackpack;
import com.terraformersmc.campanion.network.C2SRotateHeldItem;
import com.terraformersmc.campanion.platform.Services;
import com.terraformersmc.campanion.stat.CampanionStats;
import com.terraformersmc.campanion.tag.CampanionBlockTags;
import com.terraformersmc.campanion.tag.CampanionItemTags;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Campanion {

	public static final String MOD_ID = "campanion";
	public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
	public static final Logger LOG = LoggerFactory.getLogger("Campanion");

	public static CreativeModeTab TAB;

    public static void init() {
		TAB = Services.PLATFORM.createItemGroup("items", () -> CampanionItems.SMORE.asItem().getDefaultInstance());


		CampanionConfigManager.initializeConfig();

		CampanionCriteria.loadClass();
		CampanionStats.loadClass();

		registerServerboundPackets();

		DispenserBlock.registerBehavior(CampanionItems.SKIPPING_STONE, new AbstractProjectileDispenseBehavior() {
			@Override
			protected Projectile getProjectile(Level world, Position position, ItemStack stack) {
				return Util.make(new SkippingStoneEntity(world, position.x(), position.y(), position.z()), (snowballEntity) -> {
					snowballEntity.setItem(stack);
				});
			}
		});

		DispenserBlock.registerBehavior(CampanionItems.FLARE, new AbstractProjectileDispenseBehavior() {
			@Override
			protected Projectile getProjectile(Level world, Position position, ItemStack stack) {
				return Util.make(new FlareEntity(world, position.x(), position.y(), position.z()), (flareEntity) -> {
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
