package net.darkhax.tips.client.gui;

import java.util.Collections;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class GuiFactoryTip implements IModGuiFactory {
    
    @Override
    public void initialize (Minecraft mc) {
        
        // not used yet.
    }
    
    @Override
    public boolean hasConfigGui () {
        
        return true;
    }
    
    @Override
    public GuiScreen createConfigGui (GuiScreen parent) {
        
        return new GuiConfigTip(parent);
    }
    
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories () {
        
        return Collections.<RuntimeOptionCategoryElement> emptySet();
    }
}