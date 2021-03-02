package net.darkhax.tips.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ListEntryCategory extends ListEntry {
    
    private final ITextComponent modName;
    private final int labelWidth;
    
    public ListEntryCategory(Minecraft mc, ITextComponent modName) {
        
        super(mc);
        this.modName = modName;
        this.labelWidth = this.mc.fontRenderer.getStringPropertyWidth(this.modName);
    }
    
    @Override
    public void render (MatrixStack matrix, int index, int yStart, int xStart, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
        
        this.mc.fontRenderer.func_243248_b(matrix, this.modName, this.mc.currentScreen.width / 2 - this.labelWidth / 2, yStart + height - 9 - 1, 0xffffff);
    }
    
    @Override
    public boolean changeFocus (boolean focus) {
        
        return false;
    }
}