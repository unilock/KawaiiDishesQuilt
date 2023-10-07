package com.hakimen.kawaiidishes.client.screens;

import com.hakimen.kawaiidishes.KawaiiDishes;
import com.hakimen.kawaiidishes.containers.IceCreamMachineContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class IceCreamScreen extends AbstractContainerScreen<IceCreamMachineContainer>{

    private final ResourceLocation GUI = new ResourceLocation(KawaiiDishes.modId, "textures/gui/ice_cream_machine_gui.png");

    public IceCreamScreen(IceCreamMachineContainer container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        this.renderBg(guiGraphics,partialTicks,mouseX,mouseY);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.blit(GUI, relX+89, relY+35, 176, 0,menu.getScaledProgress(),20);

    }
}

