package com.terraformersmc.campanion.data;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.damagesource.CampanionDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CampanionDamageTypeTagsGenerator extends DamageTypeTagsProvider {
	public CampanionDamageTypeTagsGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
		super(packOutput, provider, Campanion.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(DamageTypeTags.IS_PROJECTILE).add(CampanionDamageTypes.SPEAR);
		this.tag(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS).add(CampanionDamageTypes.SPEAR);
		this.tag(DamageTypeTags.PANIC_CAUSES).add(CampanionDamageTypes.SPEAR);
		this.tag(Tags.DamageTypes.IS_PHYSICAL).add(CampanionDamageTypes.SPEAR);
	}
}
