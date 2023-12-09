package com.hakimen.kawaiidishes;

import com.hakimen.kawaiidishes.config.KawaiiDishesClientConfig;
import com.hakimen.kawaiidishes.config.KawaiiDishesCommonConfig;
import com.hakimen.kawaiidishes.mixin.EntityAccessor;
import com.hakimen.kawaiidishes.registry.BlockEntityRegister;
import com.hakimen.kawaiidishes.registry.ItemRegister;
import com.hakimen.kawaiidishes.registry.PacketRegister;
import com.hakimen.kawaiidishes.registry.Registration;
import com.hakimen.kawaiidishes.utils.MaidMobEventHandler;
import com.hakimen.kawaiidishes.world.VillageStructures;
import com.mojang.logging.LogUtils;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityEvents;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.fml.config.ModConfig;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents;
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

        this.setup();

        EntityEvents.ON_JOIN_WORLD.register(this::onLivingSpecialSpawn);
        ServerLifecycleEvents.READY.register(VillageStructures::addNewVillageBuilding);

        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.inventory, BlockEntityRegister.blender.get());
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.inventory, BlockEntityRegister.coffeeMachine.get());
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.inventory, BlockEntityRegister.coffeePress.get());
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.inventory, BlockEntityRegister.iceCreamMachine.get());
    }

    // TODO: not sure if this works
    public boolean onLivingSpecialSpawn(Entity entity, Level world, boolean loadedFromDisk) {
        if (((EntityAccessor) entity).isFirstTick() && entity instanceof Monster monster && !entity.serializeNBT().getBoolean("isBaby") && RANDOM.nextFloat(0,1) < KawaiiDishesCommonConfig.chanceToSpawnWithDress.get()) {
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

        return false;
    }

    private void setup() {
        ComposterBlock.COMPOSTABLES.put(ItemRegister.coffeeFruit.get(),0.25f);
        ComposterBlock.COMPOSTABLES.put(ItemRegister.driedCoffeeBeans.get(),0.50f);
        ComposterBlock.COMPOSTABLES.put(ItemRegister.roastedCoffeeBeans.get(),0.75f);

        ComposterBlock.COMPOSTABLES.put(ItemRegister.driedCocoaBeans.get(),0.50f);
        ComposterBlock.COMPOSTABLES.put(ItemRegister.roastedCocoaBeans.get(),0.75f);
    }
}
