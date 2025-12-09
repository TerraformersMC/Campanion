package com.terraformersmc.campanion.client;

import com.terraformersmc.campanion.client.model.block.BridgePlanksUnbakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

public class BridgePlanksUnbakedGeometry extends BridgePlanksUnbakedModel implements IUnbakedGeometry<BridgePlanksUnbakedGeometry> {

	/*
	@Override
	public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		return this.getMaterials(modelGetter, missingTextureErrors);
	}
	 */

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
		this.resolveParents(modelGetter);
	}

	@Override
	public BakedModel bake(IGeometryBakingContext iGeometryBakingContext, ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides itemOverrides) {
		return this.bake(bakery, spriteGetter, modelState);
	}
}
