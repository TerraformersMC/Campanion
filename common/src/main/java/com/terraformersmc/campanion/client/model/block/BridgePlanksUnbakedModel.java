package com.terraformersmc.campanion.client.model.block;

import java.util.*;
import java.util.function.Function;

import com.terraformersmc.campanion.platform.services.ClientServices;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class BridgePlanksUnbakedModel implements UnbakedModel {
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> function) {
	}

	@Nullable
	@Override
	public BakedModel bake(ModelBaker modelBaker, Function<Material, TextureAtlasSprite> function, ModelState modelState) {
		return ClientServices.CLIENT_PLATFORM.createPlanksModel();
	}

	/*
	@Override
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        List<Material> list = new ArrayList<>();
        Collections.addAll(list, BridgePlanksBakedModel.PLANKS);
        list.add(BridgePlanksBakedModel.ROPE);
        list.add(BridgePlanksBakedModel.STOPPER);
        return list;
    }
	 */
}
