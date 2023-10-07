package com.hakimen.kawaiidishes.registry;

import com.hakimen.kawaiidishes.effects.KawaiiEffect;
import com.hakimen.kawaiidishes.effects.NekoEffect;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;

import static com.hakimen.kawaiidishes.KawaiiDishes.modId;

public class EffectRegister {
    public static final LazyRegistrar<MobEffect> MOB_EFFECTS = LazyRegistrar.create(Registries.MOB_EFFECT, modId);

    public static final RegistryObject<KawaiiEffect> kawaiiEffect = MOB_EFFECTS.register("kawaii", KawaiiEffect::new);
    public static final RegistryObject<NekoEffect> nekoEffect = MOB_EFFECTS.register("neko", NekoEffect::new);


    public static void register(){
        MOB_EFFECTS.register();
    }
}
