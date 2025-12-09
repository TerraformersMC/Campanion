package com.terraformersmc.campanion.platform;

import com.mojang.datafixers.types.Type;
import com.terraformersmc.campanion.platform.services.IPlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.Tags;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

	@Override
	public boolean isOptifineLoaded() {
		return false;//TODO: figure out how to do
	}

	@Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

	@Override
	public <T extends BlockEntity> Function<Type<?>, BlockEntityType<T>> createBlockEntity(BiFunction<BlockPos, BlockState, T> function, Block... blocks) {
		return BlockEntityType.Builder.of(function::apply, blocks)::build;
	}

	@Override
	public TagKey<Item> getShearsTag() {
		return Tags.Items.TOOLS_SHEAR;
	}
}
