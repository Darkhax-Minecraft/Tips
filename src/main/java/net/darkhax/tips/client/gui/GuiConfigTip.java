package net.darkhax.tips.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.tips.TipsMod;
import net.darkhax.tips.config.Config;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiConfigTip extends GuiConfig {
    
    private static Configuration cfg = Config.config;
    
    public GuiConfigTip(GuiScreen parent) {
        
        super(parent, generateConfigList(), TipsMod.MODID, false, false, GuiConfig.getAbridgedConfigPath(cfg.toString()));
    }
    
    public static List<IConfigElement> generateConfigList () {
        
        final ArrayList<IConfigElement> elements = new ArrayList<>();
        
        for (final String name : cfg.getCategoryNames()) {
            
            elements.add(new ConfigElement(cfg.getCategory(name)));
        }
        
        return elements;
    }
}