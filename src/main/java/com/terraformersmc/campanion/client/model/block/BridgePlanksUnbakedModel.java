package com.terraformersmc.campanion.client.model.block;

import com.mojang.datafixers.util.Pair;
import java.util.*;
import java.util.function.Function;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

public class BridgePlanksUnbakedModel implements UnbakedModel {
    @Override
    public Collection<ResourceLocation> getDependencies() {
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
    public BakedModel bake(ModelBakery loader, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer, ResourceLocation modelId) {
        return new BridgePlanksBakedModel();
    }
}
