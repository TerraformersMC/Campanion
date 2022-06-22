package com.terraformersmc.campanion.recipe;

import com.terraformersmc.campanion.Campanion;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

public class CampanionRecipeSerializers {

    private static final Map<ResourceLocation, RecipeSerializer<?>> RECIPE_SERIALIZERS = new LinkedHashMap<>();

	public static final SimpleRecipeSerializer<TentBuildingRecipe> TENT_BUILDING_RECIPE = add("tent_building_recipe", new SimpleRecipeSerializer<>(TentBuildingRecipe::new));
	public static final SimpleRecipeSerializer<TarpRecipe> TARP_RECIPE = add("tarp_recipe", new SimpleRecipeSerializer<>(TarpRecipe::new));

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S add(String id, S serializer) {
		RECIPE_SERIALIZERS.put(new ResourceLocation(Campanion.MOD_ID, id), serializer);
        return serializer;
    }

    public static void register() {
        for (ResourceLocation identifier : RECIPE_SERIALIZERS.keySet()) {
            Registry.register(Registry.RECIPE_SERIALIZER, identifier, RECIPE_SERIALIZERS.get(identifier));
        }
    }

}
