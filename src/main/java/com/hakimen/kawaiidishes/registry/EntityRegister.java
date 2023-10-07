package com.hakimen.kawaiidishes.registry;

import com.hakimen.kawaiidishes.entity.SittableEntity;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import static com.hakimen.kawaiidishes.KawaiiDishes.modId;

public class EntityRegister {
    public static final LazyRegistrar<EntityType<?>> ENTITIES = LazyRegistrar.create(Registries.ENTITY_TYPE,modId);

    public static final RegistryObject<EntityType<SittableEntity>> SEAT = ENTITIES.register("seat",
            () -> EntityType.Builder.<SittableEntity>of(SittableEntity::new, MobCategory.MISC).sized(1f, 1f)
                    .build(new ResourceLocation(modId, "seat").toString()));

    public static void register(){
        ENTITIES.register();
    }
}
