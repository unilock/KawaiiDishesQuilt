package com.hakimen.kawaiidishes;

import com.hakimen.kawaiidishes.client.screens.BlenderScreen;
import com.hakimen.kawaiidishes.client.screens.CoffeeMachineScreen;
import com.hakimen.kawaiidishes.client.screens.IceCreamScreen;
import com.hakimen.kawaiidishes.config.KawaiiDishesClientConfig;
import com.hakimen.kawaiidishes.config.KawaiiDishesCommonConfig;
import com.hakimen.kawaiidishes.registry.ContainerRegister;
import com.hakimen.kawaiidishes.registry.ItemRegister;
import com.hakimen.kawaiidishes.registry.PacketRegister;
import com.hakimen.kawaiidishes.registry.Registration;
import com.hakimen.kawaiidishes.utils.MaidMobEventHandler;
import com.mojang.logging.LogUtils;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;

import java.util.Random;

public class KawaiiDishes implements ModInitializer {
    // Directly reference a slf4j logger
    public static final Random RANDOM = new Random();
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String modId = "kawaiidishes";

	@Override
	public void onInitialize(ModContainer mod) {
		ForgeConfigRegistry.INSTANCE.register(modId, ModConfig.Type.CLIENT, KawaiiDishesClientConfig.clientSpec, "kawaii-dishes-client.toml");
		ForgeConfigRegistry.INSTANCE.register(modId, ModConfig.Type.COMMON, KawaiiDishesCommonConfig.commonSpec, "kawaii-dishes-common.toml");

		Registration.init();
		PacketRegister.register();
	}

    public void onLivingSpecialSpawn(MobSpawnEvent event) {
        Entity entity = event.getEntity();
        if (!entity.isAddedToWorld() && entity instanceof Monster monster && !entity.serializeNBT().getBoolean("isBaby") && RANDOM.nextFloat(0,1) < KawaiiDishesCommonConfig.chanceToSpawnWithDress.get()) {
            if((monster instanceof Skeleton
                    || monster instanceof WitherSkeleton
                    || monster instanceof Stray
                    || monster instanceof Zombie
                    || monster instanceof Piglin
                    || monster instanceof PiglinBrute) && KawaiiDishesCommonConfig.shouldMobSpawnWithDress.get()){
                ItemStack[] stacks = MaidMobEventHandler.armorBuild(RANDOM);

                monster.setItemSlot(EquipmentSlot.HEAD, stacks[0]);
                monster.setItemSlot(EquipmentSlot.CHEST, stacks[1]);
                monster.setItemSlot(EquipmentSlot.LEGS, stacks[2]);
                monster.setItemSlot(EquipmentSlot.FEET, stacks[3]);

                monster.setDropChance(EquipmentSlot.HEAD,KawaiiDishesCommonConfig.chanceToDropArmorSet.get().floatValue());
                monster.setDropChance(EquipmentSlot.CHEST,KawaiiDishesCommonConfig.chanceToDropArmorSet.get().floatValue());
                monster.setDropChance(EquipmentSlot.LEGS,KawaiiDishesCommonConfig.chanceToDropArmorSet.get().floatValue());
                monster.setDropChance(EquipmentSlot.FEET,KawaiiDishesCommonConfig.chanceToDropArmorSet.get().floatValue());
            }
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        ComposterBlock.COMPOSTABLES.put(ItemRegister.coffeeFruit.get(),0.25f);
        ComposterBlock.COMPOSTABLES.put(ItemRegister.driedCoffeeBeans.get(),0.50f);
        ComposterBlock.COMPOSTABLES.put(ItemRegister.roastedCoffeeBeans.get(),0.75f);

        ComposterBlock.COMPOSTABLES.put(ItemRegister.driedCocoaBeans.get(),0.50f);
        ComposterBlock.COMPOSTABLES.put(ItemRegister.roastedCocoaBeans.get(),0.75f);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    }

    private void processIMC(final InterModProcessEvent event) {

    }
    private void clientStartup(final FMLClientSetupEvent event){
        event.enqueueWork(() -> {
            MenuScreens.register(ContainerRegister.coffeeMachine.get(), CoffeeMachineScreen::new);
            MenuScreens.register(ContainerRegister.iceCreamMachine.get(), IceCreamScreen::new);
            MenuScreens.register(ContainerRegister.blenderContainer.get(), BlenderScreen::new);
        });
    }
}
