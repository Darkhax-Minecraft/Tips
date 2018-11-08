package net.darkhax.tips.config;

import java.io.File;

import net.darkhax.tips.TipsAPI;
import net.darkhax.tips.TipsMod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = TipsMod.MODID)
public class Config {
    
    public static Configuration config = new Configuration(new File("config/" + TipsMod.MODID + ".cfg"));
    
    public static boolean allowDefaultTips = true;
    public static int xOffset = 5;
    public static int yOffset = 40;
    public static int titleColor = 0xFFFF55;
    public static int textColor = 0xFFFFFF;
    public static String[] tips = new String[0];
    
    public static void syncConfigData () {
        
        xOffset = config.getInt("xOffset", "general", 5, 0, Integer.MAX_VALUE, "The amount of offset the tip text should have from the left of the screen.");
        yOffset = config.getInt("yOffset", "general", 40, 0, Integer.MAX_VALUE, "The amount of offset the tip text should have from the bottom of the screen.");
        titleColor = readColor("titleColor", "FFFF55", "The color of the top/title text for the tip.");
        textColor = readColor("textColor", "FFFFFF", "The color of the actual tip text.");
        allowDefaultTips = config.getBoolean("allowDefaultTips", "general", true, "Determines whether or not the default tips should be possible.");
        tips = config.getStringList("customTips", "general", new String[0], "A list of custom tips added by the user or modpack.");
        
        if (config.hasChanged()) {
            
            config.save();
        }
    }
    
    private static int readColor (String name, String def, String comment) {
        
        final String colorInput = config.getString(name, "general", def, comment);
        
        try {
            
            return Integer.parseInt(colorInput, 16);
        }
        
        catch (Exception e) {
            
            TipsMod.LOG.error("Failed to read color {} for {}. Please ensure it's a valid hex color!", name, colorInput);
            return 0;
        }
    }
    
    @SubscribeEvent
    public static void onConfigReload (ConfigChangedEvent.OnConfigChangedEvent event) {
        
        if (TipsMod.MODID.equalsIgnoreCase(event.getModID())) {
            
            syncConfigData();
            TipsAPI.reloadTips();
        }
    }
}