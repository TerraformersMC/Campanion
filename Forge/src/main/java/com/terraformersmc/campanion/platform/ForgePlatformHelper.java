package com.terraformersmc.campanion.platform;

import com.mojang.datafixers.types.Type;
import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.platform.rendering.ForgeBlockModelPartCreator;
import com.terraformersmc.campanion.platform.services.IPlatformHelper;
import com.terraformersmc.campanion.platform.services.rendering.BlockModelPartCreator;
import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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
	public CreativeModeTab createItemGroup(String name, Supplier<ItemStack> stack) {
		return new CreativeModeTab(String.format("%s.%s", Campanion.MOD_ID, name)) {
			@Override
			public ItemStack makeIcon() {
				return stack.get();
			}
		};
	}

	@Override
	public TagKey<Item> getShearsTag() {
		return Tags.Items.SHEARS;
	}
}
