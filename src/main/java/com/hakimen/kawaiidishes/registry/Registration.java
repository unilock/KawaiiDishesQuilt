package com.hakimen.kawaiidishes.registry;

public class Registration {
    public static void init(){
        BlockRegister.register();
        BlockEntityRegister.register();
        ItemRegister.register();
        ContainerRegister.register();
        RecipeRegister.register();
        EffectRegister.register();
        EntityRegister.register();
        VillagerWorkRegister.register();
    }
}
