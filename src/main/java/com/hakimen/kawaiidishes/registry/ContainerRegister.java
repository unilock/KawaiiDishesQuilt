package com.hakimen.kawaiidishes.registry;

import com.hakimen.kawaiidishes.KawaiiDishes;
import com.hakimen.kawaiidishes.containers.BlenderContainer;
import com.hakimen.kawaiidishes.containers.CoffeeMachineContainer;
import com.hakimen.kawaiidishes.containers.IceCreamMachineContainer;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class ContainerRegister {
    public static final LazyRegistrar<MenuType<?>> CONTAINERS = LazyRegistrar.create(Registries.MENU, KawaiiDishes.modId);

    public static final RegistryObject<MenuType<CoffeeMachineContainer>> coffeeMachine = CONTAINERS.register("coffee_machine", ()-> IForgeMenuType.create(CoffeeMachineContainer::new));
    public static final RegistryObject<MenuType<IceCreamMachineContainer>> iceCreamMachine = CONTAINERS.register("ice_cream_machine", ()-> IForgeMenuType.create(IceCreamMachineContainer::new));
    public static final RegistryObject<MenuType<BlenderContainer>> blenderContainer = CONTAINERS.register("blender", ()-> IForgeMenuType.create(BlenderContainer::new));

    public static void register(){
        CONTAINERS.register();
    }
}
