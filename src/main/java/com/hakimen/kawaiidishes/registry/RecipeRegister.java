package com.hakimen.kawaiidishes.registry;

import com.hakimen.kawaiidishes.KawaiiDishes;
import com.hakimen.kawaiidishes.recipes.BlenderRecipe;
import com.hakimen.kawaiidishes.recipes.CoffeeMachineRecipe;
import com.hakimen.kawaiidishes.recipes.CoffeePressRecipe;
import com.hakimen.kawaiidishes.recipes.IceCreamMachineRecipe;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RecipeRegister {
    public static final LazyRegistrar<RecipeSerializer<?>> SERIALIZERS =
            LazyRegistrar.create(Registries.RECIPE_SERIALIZER, KawaiiDishes.modId);

    public static final RegistryObject<RecipeSerializer<CoffeePressRecipe>> CoffeePressRecipeSerializer =
            SERIALIZERS.register("coffee_pressing", () -> CoffeePressRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<CoffeeMachineRecipe>> CoffeeMachineRecipeSerializer =
            SERIALIZERS.register("coffee_machining", () -> CoffeeMachineRecipe.Serializer.INSTANCE);


    public static final RegistryObject<RecipeSerializer<IceCreamMachineRecipe>> IceCreamMakingRecipe =
            SERIALIZERS.register("ice_cream_making", () -> IceCreamMachineRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<BlenderRecipe>> BlendingRecipe =
            SERIALIZERS.register("blending", () -> BlenderRecipe.Serializer.INSTANCE);


    public static void register() {
        SERIALIZERS.register();
    }
}
