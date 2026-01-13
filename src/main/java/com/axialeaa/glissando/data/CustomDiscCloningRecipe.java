package com.axialeaa.glissando.data;

import com.axialeaa.glissando.data.registry.GlissandoRecipes;
import com.axialeaa.glissando.item.component.CustomDiscComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CustomDiscCloningRecipe extends SpecialCraftingRecipe {

    public static final String PATH = "custom_disc_cloning";

	public CustomDiscCloningRecipe(CraftingRecipeCategory craftingRecipeCategory) {
		super(craftingRecipeCategory);
	}

	public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
		if (craftingRecipeInput.getStackCount() != 2)
			return false;

        boolean foundDisc = false;
        boolean componentContainsData = false;

        for (ItemStack itemStack : craftingRecipeInput.getStacks()) {
            if (itemStack.isEmpty())
                continue;

            boolean hasDiscComponent = itemStack.contains(CustomDiscComponent.TYPE);

            if (!hasDiscComponent)
                continue;

            CustomDiscComponent component = itemStack.get(CustomDiscComponent.TYPE);
            assert component != null;

            if (!component.data().isEmpty())
                componentContainsData = true;
            else if (foundDisc)
                continue;

            foundDisc = true;
        }

        return foundDisc && componentContainsData;
    }

	public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup wrapperLookup) {
        for (ItemStack itemStack : input.getStacks()) {
            if (itemStack.isEmpty())
                continue;

            CustomDiscComponent component = itemStack.getOrDefault(CustomDiscComponent.TYPE, CustomDiscComponent.DEFAULT);

            if (component.data().isEmpty())
                continue;

            return itemStack.copy();
        }

		return ItemStack.EMPTY;
	}

    @Override
    public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput input) {
        ItemStack nonEmptyDataStack = ItemStack.EMPTY;
        int emptyDataStackSlot = -1;

        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack itemStack = input.getStackInSlot(slot);

            if (itemStack.isEmpty())
                continue;

            CustomDiscComponent component = itemStack.getOrDefault(CustomDiscComponent.TYPE, CustomDiscComponent.DEFAULT);

            if (component.data().isEmpty())
                emptyDataStackSlot = slot;
            else
                nonEmptyDataStack = itemStack.copy();
        }

        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(input.size(), ItemStack.EMPTY);
        defaultedList.set(emptyDataStackSlot, nonEmptyDataStack);

        return defaultedList;
    }

    @Override
	public RecipeSerializer<CustomDiscCloningRecipe> getSerializer() {
		return GlissandoRecipes.CUSTOM_DISC_CLONING;
	}

}