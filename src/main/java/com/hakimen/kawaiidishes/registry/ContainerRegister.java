package com.hakimen.kawaiidishes.registry;

import com.hakimen.kawaiidishes.KawaiiDishes;
import com.hakimen.kawaiidishes.containers.BlenderContainer;
import com.hakimen.kawaiidishes.containers.CoffeeMachineContainer;
import com.hakimen.kawaiidishes.containers.IceCreamMachineContainer;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class ContainerRegister {
    public static final LazyRegistrar<MenuType<?>> CONTAINERS = LazyRegistrar.create(Registries.MENU, KawaiiDishes.modId);

    public static final RegistryObject<MenuType<CoffeeMachineContainer>> coffeeMachine = CONTAINERS.register("coffee_machine", ()-> new ExtendedScreenHandlerType<>(CoffeeMachineContainer::new));
    public static final RegistryObject<MenuType<IceCreamMachineContainer>> iceCreamMachine = CONTAINERS.register("ice_cream_machine", ()-> new ExtendedScreenHandlerType<>(IceCreamMachineContainer::new));
    public static final RegistryObject<MenuType<BlenderContainer>> blenderContainer = CONTAINERS.register("blender", ()-> new ExtendedScreenHandlerType<>(BlenderContainer::new));

    public static void register(){
        CONTAINERS.register();
    }
}
