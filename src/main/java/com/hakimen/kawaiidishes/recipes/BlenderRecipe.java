package com.hakimen.kawaiidishes.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hakimen.kawaiidishes.KawaiiDishes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class BlenderRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final int ticks;
    private final ItemStack onOutput;

    public ItemStack getOnOutput() {
        return onOutput;
    }

    public BlenderRecipe(ResourceLocation id, ItemStack output,
                         NonNullList<Ingredient> recipeItems, int ticks, ItemStack onOutput) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.ticks = ticks;
        this.onOutput = onOutput;
    }

    public int getTicks() {
        return ticks;
    }


    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        boolean match = true;

        if(!pContainer.getItem(pContainer.getContainerSize()-1).getItem().equals(getResultItem(null).getItem())
                && !pContainer.getItem(pContainer.getContainerSize()-1).is(ItemStack.EMPTY.getItem())) {
            match = false;
        }
        if(!onOutput.equals(ItemStack.EMPTY)) {
            if(onOutput.getItem() == pContainer.getItem(pContainer.getContainerSize()-1).getItem()){
                match = true;
            }else
                return false;
        }

        boolean submatches[] = new boolean[] {false,false};

        if(recipeItems.get(0).getItems().length == 2) {
            submatches[0] = recipeItems.get(0).getItems()[0].getItem().equals(pContainer.getItem(0).getItem());
            submatches[1] = recipeItems.get(0).getItems()[1].getItem().equals(pContainer.getItem(1).getItem());
        }else if(recipeItems.get(0).getItems().length == 1) {
            submatches[0] = recipeItems.get(0).getItems()[0].getItem().equals(pContainer.getItem(0).getItem());
            submatches[1] = pContainer.getItem(1).getItem().equals(Items.AIR);
        }
        for (int i = 0; i < submatches.length; i++) {
            match &= submatches[i];
        }
        return match;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess p_267165_) {
        return output;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }


    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    public static class Type implements RecipeType<BlenderRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "blending";
    }

    public static class Serializer implements RecipeSerializer<BlenderRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(KawaiiDishes.modId,"blending");

        @Override
        public BlenderRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            ItemStack onOutput = ItemStack.EMPTY;
            if(json.has("onOutput")){
                onOutput = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "onOutput"));
            }

            int ticks = GsonHelper.getAsInt(json,"ticks");

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(3, Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) {

                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new BlenderRecipe(id, output, inputs, ticks,onOutput);
        }

        @Override
        public BlenderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }
            int ticks = buf.readInt();
            ItemStack output = buf.readItem();
            ItemStack onOutput = buf.readItem();
            return new BlenderRecipe(id, output, inputs,ticks,onOutput);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BlenderRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeInt(recipe.ticks);
            buf.writeItem(recipe.getResultItem(null));
            buf.writeItem(recipe.onOutput);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}
