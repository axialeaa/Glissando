package com.axialeaa.glissando.data.registry;

import com.axialeaa.glissando.data.CustomDiscCloningRecipe;
import com.axialeaa.glissando.item.MusicDiscItem;
import net.minecraft.data.recipe.ComplexRecipeJsonBuilder;
import net.minecraft.data.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

public class GlissandoRecipeGenerator extends RecipeGenerator {

    protected GlissandoRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
    }

    @Override
    public void generate() {
        this.createShaped(RecipeCategory.MISC, MusicDiscItem.REGISTRY_INSTANCE)
            .pattern(" K ")
            .pattern("KGK")
            .pattern(" K ")
            .input('K', Items.DRIED_KELP)
            .input('G', Items.GUNPOWDER)
            .criterion(hasItem(Items.DRIED_KELP), this.conditionsFromItem(Items.DRIED_KELP))
            .criterion(hasItem(Items.GUNPOWDER), this.conditionsFromItem(Items.GUNPOWDER))
            .offerTo(this.exporter);

        CookingRecipeJsonBuilder
            .createSmelting(
                Ingredient.ofItem(MusicDiscItem.REGISTRY_INSTANCE),
                RecipeCategory.MISC,
                Items.GUNPOWDER,
                0.1F,
                200
            )
            .criterion(hasItem(MusicDiscItem.REGISTRY_INSTANCE), this.conditionsFromItem(MusicDiscItem.REGISTRY_INSTANCE))
            .offerTo(this.exporter);

        ComplexRecipeJsonBuilder
            .create(CustomDiscCloningRecipe::new)
            .offerTo(this.exporter, CustomDiscCloningRecipe.PATH);
    }

}
