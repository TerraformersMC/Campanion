package com.terraformersmc.campanion.damagesource;

import com.terraformersmc.campanion.Campanion;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class CampanionDamageTypes {
	public static final ResourceKey<DamageType> SPEAR = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Campanion.MOD_ID, "spear"));

	public static void bootstrap(BootstrapContext<DamageType> pContext) {
		pContext.register(SPEAR, new DamageType("spear", 0.1F));
	}
}
