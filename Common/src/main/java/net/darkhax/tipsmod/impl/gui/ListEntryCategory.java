package net.darkhax.tipsmod.impl.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class ListEntryCategory extends ListEntry {
    
    private final Component modName;
    private final int labelWidth;
    
    public ListEntryCategory(Minecraft mc, Component modName) {
        
        super(mc);
        this.modName = modName;
        this.labelWidth = this.mc.font.width(this.modName);
    }
    
    @Override
    public void render (PoseStack matrix, int index, int yStart, int xStart, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
        
        this.mc.font.draw(matrix, this.modName, (this.mc.screen.width / 2f) - (this.labelWidth / 2f), yStart + height - 9 - 1, 0xffffff);
    }
    
    @Override
    public boolean changeFocus (boolean focus) {
        
        return false;
    }

    @Override
    public void updateNarrator(NarrationElementOutput narrator) {

        narrator.add(NarratedElementType.TITLE, this.modName);
    }
}