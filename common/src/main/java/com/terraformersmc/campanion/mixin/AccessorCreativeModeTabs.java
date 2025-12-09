package com.terraformersmc.campanion.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreativeModeTabs.class)
public interface AccessorCreativeModeTabs {

	@Accessor
	static ResourceKey<CreativeModeTab> getFOOD_AND_DRINKS(){
		throw new AssertionError("AccessorCreativeModeTabs not applied!");
	}

	@Accessor
	static ResourceKey<CreativeModeTab> getCOMBAT(){
		throw new AssertionError("AccessorCreativeModeTabs not applied!");
	}

	@Accessor
	static ResourceKey<CreativeModeTab> getTOOLS_AND_UTILITIES(){
		throw new AssertionError("AccessorCreativeModeTabs not applied!");
	}

	@Accessor
	static ResourceKey<CreativeModeTab> getINGREDIENTS(){
		throw new AssertionError("AccessorCreativeModeTabs not applied!");
	}
}
