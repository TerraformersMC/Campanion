package com.terraformersmc.campanion.mixin;

import net.minecraft.item.Item;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemTags.class)
public interface AccessorItemTags {
	@Invoker
	static Tag.Identified<Item> callRegister(String id) {
		return null;
	}
}
