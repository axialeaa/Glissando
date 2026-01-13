package com.axialeaa.glissando.data.registry;

import com.axialeaa.glissando.data.CustomDiscCloningRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public interface GlissandoRecipes {

    String CUSTOM_DISC_CLONING_PATH = "crafting_recipe_special_customdisccloning";
    RecipeSerializer<CustomDiscCloningRecipe> CUSTOM_DISC_CLONING = new SpecialCraftingRecipe.SpecialRecipeSerializer<>(CustomDiscCloningRecipe::new);

    class Provider extends FabricRecipeProvider {

        public Provider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
            return new GlissandoRecipeGenerator(registryLookup, exporter);
        }

        @Override
        public String getName() {
            return "Recipe Provider";
        }

    }

}
