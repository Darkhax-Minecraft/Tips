package net.darkhax.tipsmod.impl.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;

public abstract class ListEntry extends AbstractSelectionList.Entry<ListEntry> {
    
    public final Minecraft mc;
    protected boolean isSelected;
    
    public ListEntry(Minecraft mc) {

        this.mc = mc;
    }
    
    public void renderMouseOver (PoseStack matrix, int mouseX, int mouseY) {
        
    }

    public abstract void updateNarrator(NarrationElementOutput narrator);
}