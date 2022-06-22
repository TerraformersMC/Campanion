package com.terraformersmc.campanion.platform;

import com.mojang.datafixers.types.Type;
import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.platform.rendering.ForgeBlockModelPartCreator;
import com.terraformersmc.campanion.platform.services.IClientPlatformHelper;
import com.terraformersmc.campanion.platform.services.IPlatformHelper;
import com.terraformersmc.campanion.platform.services.rendering.BlockModelPartCreator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeClientPlatformHelper implements IClientPlatformHelper {

	@Override
	public BlockModelPartCreator blockModelCreator() {
		return new ForgeBlockModelPartCreator();
	}
}
