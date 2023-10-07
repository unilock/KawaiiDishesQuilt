package com.hakimen.kawaiidishes.registry;

import com.google.common.collect.ImmutableSet;
import com.hakimen.kawaiidishes.KawaiiDishes;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;

public class VillagerWorkRegister {
    public static final LazyRegistrar<PoiType> POI_TYPES = LazyRegistrar.create(Registries.POINT_OF_INTEREST_TYPE, KawaiiDishes.modId);
    public static final LazyRegistrar<VillagerProfession> VILLAGER_PROFESSIONS = LazyRegistrar.create(Registries.VILLAGER_PROFESSION,  KawaiiDishes.modId);

    public static final RegistryObject<PoiType> baristaPOI = POI_TYPES.register("barista", () -> new PoiType(ImmutableSet.copyOf(BlockRegister.coffeeMachine.get().getStateDefinition().getPossibleStates()), 1, 1));
    public static final RegistryObject<VillagerProfession> baristaVillagerProfession = VILLAGER_PROFESSIONS.register("barista", () -> new VillagerProfession(KawaiiDishes.modId + ":barista", holder -> holder.is(baristaPOI.getKey()), holder -> holder.is(baristaPOI.getKey()), ImmutableSet.of(), ImmutableSet.of(BlockRegister.coffeeMachine.get()), SoundEvents.VILLAGER_WORK_CLERIC));


    public static void register() {
        VILLAGER_PROFESSIONS.register();
        POI_TYPES.register();
    }

}
