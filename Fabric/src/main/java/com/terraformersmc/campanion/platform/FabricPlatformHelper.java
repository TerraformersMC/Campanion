package com.terraformersmc.campanion.platform;

import com.mojang.datafixers.types.Type;
import com.terraformersmc.campanion.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

	@Override
	public boolean isOptifineLoaded() {
		return isModLoaded("optifabric");
	}

	@Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

	@Override
	public <T extends BlockEntity> Function<Type<?>, BlockEntityType<T>> createBlockEntity(BiFunction<BlockPos, BlockState, T> function, Block... blocks) {
		return FabricBlockEntityTypeBuilder.create(function::apply, blocks)::build;
	}

	@Override
	public CreativeModeTab createItemGroup(String name, Supplier<ItemStack> stack) {
		return FabricItemGroup.builder().icon(stack).build();
		//new ResourceLocation(Campanion.MOD_ID, name)
	}

	@Override
	public TagKey<Item> getShearsTag() {
		return ConventionalItemTags.SHEARS;
	}
}
