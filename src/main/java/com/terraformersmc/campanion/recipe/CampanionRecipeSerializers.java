package com.terraformersmc.campanion.recipe;

import com.terraformersmc.campanion.Campanion;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class CampanionRecipeSerializers {

    private static final Map<Identifier, RecipeSerializer<?>> RECIPE_SERIALIZERS = new LinkedHashMap<>();

    public static final SpecialRecipeSerializer<TentBuildingRecipe> TENT_BUILDING_RECIPE = add("tent_building_recipe", new SpecialRecipeSerializer<>(TentBuildingRecipe::new));

    private static <S extends RecipeSerializer<T>, T extends Recipe<?>> S add(String id, S serializer) {
        RECIPE_SERIALIZERS.put(new Identifier(Campanion.MOD_ID, id), serializer);
        return serializer;
    }

    public static void register() {
        for (Identifier identifier : RECIPE_SERIALIZERS.keySet()) {
            Registry.register(Registry.RECIPE_SERIALIZER, identifier, RECIPE_SERIALIZERS.get(identifier));
        }
    }

}
