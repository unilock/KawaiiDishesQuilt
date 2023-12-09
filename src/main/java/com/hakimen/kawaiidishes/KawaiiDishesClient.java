package com.hakimen.kawaiidishes;

import com.hakimen.kawaiidishes.client.block_entity_renderers.CoffeeMachineRenderer;
import com.hakimen.kawaiidishes.client.block_entity_renderers.CoffeePressRenderer;
import com.hakimen.kawaiidishes.client.block_entity_renderers.IceCreamMachineRenderer;
import com.hakimen.kawaiidishes.client.entity.SeatRenderer;
import com.hakimen.kawaiidishes.client.screens.BlenderScreen;
import com.hakimen.kawaiidishes.client.screens.CoffeeMachineScreen;
import com.hakimen.kawaiidishes.client.screens.IceCreamScreen;
import com.hakimen.kawaiidishes.registry.BlockEntityRegister;
import com.hakimen.kawaiidishes.registry.ContainerRegister;
import com.hakimen.kawaiidishes.registry.EntityRegister;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class KawaiiDishesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        MenuScreens.register(ContainerRegister.coffeeMachine.get(), CoffeeMachineScreen::new);
        MenuScreens.register(ContainerRegister.iceCreamMachine.get(), IceCreamScreen::new);
        MenuScreens.register(ContainerRegister.blenderContainer.get(), BlenderScreen::new);

        this.registerRenderers();
    }

    public void registerRenderers(){
        BlockEntityRenderers.register(BlockEntityRegister.coffeePress.get(), CoffeePressRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegister.coffeeMachine.get(), CoffeeMachineRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegister.iceCreamMachine.get(), IceCreamMachineRenderer::new);

        EntityRendererRegistry.register(EntityRegister.SEAT.get(), SeatRenderer::new);
    }
}
