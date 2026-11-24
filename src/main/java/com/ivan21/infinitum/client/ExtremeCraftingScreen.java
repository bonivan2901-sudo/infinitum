package com.ivan21.infinitum.client;

import com.ivan21.infinitum.Infinitum;
import com.ivan21.infinitum.menu.ExtremeCraftingMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ExtremeCraftingScreen extends AbstractContainerScreen<ExtremeCraftingMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Infinitum.MODID, "textures/gui/extreme_crafting.png");
    private static final int TEX_W = 256;
    private static final int TEX_H = 288;

    public ExtremeCraftingScreen(ExtremeCraftingMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 238;
        this.imageHeight = 278;
        this.inventoryLabelX = ExtremeCraftingMenu.INV_X;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        g.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, TEX_W, TEX_H);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        super.render(g, mouseX, mouseY, partialTick);
        renderTooltip(g, mouseX, mouseY);
    }
}
