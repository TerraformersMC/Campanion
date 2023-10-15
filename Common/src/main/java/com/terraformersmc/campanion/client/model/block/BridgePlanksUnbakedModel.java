package com.terraformersmc.campanion.client.model.block;

import com.mojang.datafixers.util.Pair;
import com.terraformersmc.campanion.platform.services.ClientServices;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class BridgePlanksUnbakedModel implements UnbakedModel {
	@Override
	public @NotNull Collection<ResourceLocation> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		List<Material> list = new ArrayList<>();
		Collections.addAll(list, BridgePlanksBakedModel.PLANKS);
		list.add(BridgePlanksBakedModel.ROPE);
		list.add(BridgePlanksBakedModel.STOPPER);
		return list;
	}

	@Override
	public BakedModel bake(@NotNull ModelBaker modelBaker, @NotNull Function<Material, TextureAtlasSprite> function, @NotNull ModelState modelState, @NotNull ResourceLocation resourceLocation) {
		return ClientServices.CLIENT_PLATFORM.createPlanksModel();
	}
}
