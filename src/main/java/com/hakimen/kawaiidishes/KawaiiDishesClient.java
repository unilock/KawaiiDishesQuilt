package com.hakimen.kawaiidishes;

import com.hakimen.kawaiidishes.client.block_entity_renderers.CoffeeMachineRenderer;
import com.hakimen.kawaiidishes.client.block_entity_renderers.CoffeePressRenderer;
import com.hakimen.kawaiidishes.client.block_entity_renderers.IceCreamMachineRenderer;
import com.hakimen.kawaiidishes.client.entity.SeatRenderer;
import com.hakimen.kawaiidishes.registry.BlockEntityRegister;
import com.hakimen.kawaiidishes.registry.EntityRegister;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class KawaiiDishesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {

	}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(BlockEntityRegister.coffeePress.get(), CoffeePressRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegister.coffeeMachine.get(), CoffeeMachineRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegister.iceCreamMachine.get(), IceCreamMachineRenderer::new);

        event.registerEntityRenderer(EntityRegister.SEAT.get(), SeatRenderer::new);
    }
}
