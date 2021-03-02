package net.darkhax.tips.gui;

import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class ListEntry extends AbstractOptionList.Entry<ListEntry> {
    
    public final Minecraft mc;
    protected boolean isSelected;
    
    public ListEntry(Minecraft mc) {
        
        this.mc = mc;
    }
    
    @Override
    public List<? extends IGuiEventListener> getEventListeners () {
        
        return Collections.emptyList();
    }
    
    public void renderMouseOver (MatrixStack matrix, int mouseX, int mouseY) {
        
    }
}