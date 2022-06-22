package com.terraformersmc.campanion.platform;

import com.mojang.datafixers.types.Type;
import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.platform.rendering.FabricBlockModelPartCreator;
import com.terraformersmc.campanion.platform.services.IPlatformHelper;
import com.terraformersmc.campanion.platform.services.rendering.BlockModelPartCreator;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
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
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

	@Override
	public <T extends BlockEntity> Function<Type<?>, BlockEntityType<T>> createBlockEntity(BiFunction<BlockPos, BlockState, T> function, Block... blocks) {
		return FabricBlockEntityTypeBuilder.create(function::apply, blocks)::build;
	}

	@Override
	public CreativeModeTab createItemGroup(String name, Supplier<ItemStack> stack) {
		return FabricItemGroupBuilder.build(new ResourceLocation(Campanion.MOD_ID, name), stack);
	}

}
