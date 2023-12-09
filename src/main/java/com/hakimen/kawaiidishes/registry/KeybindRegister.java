package com.hakimen.kawaiidishes.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeybindRegister {
    public static final String keyCategory = "key.category.kawaiidishes.kawaiidishes";
    public static final String keyTailWag = "key.kawaiidishes.tail_wag";


    public static final KeyMapping tailWagKey = new KeyMapping(keyTailWag,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z, keyCategory);
}
