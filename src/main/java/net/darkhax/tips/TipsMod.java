package net.darkhax.tips;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.tips.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TipsMod.MODID, name = "Tips", version = "@VERSION@", clientSideOnly = true, certificateFingerprint = "@FINGERPRINT@", guiFactory = "net.darkhax.tips.client.gui.GuiFactoryTip")
public class TipsMod {
    
    public static final String MODID = "tips";
    
    public static final Logger LOG = LogManager.getLogger("Tips");
    
    @EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        
        TipsAPI.addTips(TipsMod::addDefaultModTips);
        TipsAPI.addTips(TipsMod::addConfigTips);
    }
    
    @EventHandler
    public void postInit (FMLPostInitializationEvent event) {
        
        Config.syncConfigData();
    }
    
    @EventHandler
    public void onClientLoadComplete (FMLLoadCompleteEvent event) {
        
        // Adds a resource reload listener to detect language and texture pack changes.
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(manager -> TipsAPI.reloadTips());
    }
    
    /**
     * This method is used to add the default tips to the tip list. Any mod is capable of
     * adding default tips, and they don't even need to add their own code to do it. This can
     * be disabled in the configuration file for the mod.
     * 
     * Default tips are loaded from the language file and will be ran through the standard
     * translation method. This mod looks for the translation key mods.tips.MODIDHERE.1 and if
     * it is found it will be added to the tip list and the number at the end will be increased
     * until no tips are found.
     * 
     * @param tips The list of tips being built.
     */
    private static void addDefaultModTips (List<String> tips) {
        
        if (Config.allowDefaultTips) {
            
            for (ModContainer container : Loader.instance().getActiveModList()) {
                
                final String baseKey = "mods.tips." + container.getModId() + ".";
                
                for (int i = 1; i < 10000; i++) {
                    
                    // Generate a translation key using the mod id and index. Try to translate
                    // it.
                    final String translationKey = baseKey + i;
                    final String translationTip = I18n.format(translationKey);
                    
                    // If it has been translated, add it to the list.
                    if (!translationKey.equals(translationTip)) {
                        
                        // Do default for tips added by this mod
                        if (TipsMod.MODID.equals(container.getModId())) {
                            
                            tips.add(translationTip);
                        }
                        
                        // Applies a special title for when a mod provides their own tips.
                        else {
                            
                            tips.add(translationTip + "#SPLIT#" + container.getName() + " " + I18n.format("tips.gui.title"));
                        }
                    }
                    
                    // Tip can not be found, so break the loop.
                    else {
                        
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * This method is used to add the config specified tips to the tip list.
     * 
     * @param tips The list of tips being built.
     */
    private static void addConfigTips (List<String> tips) {
        
        for (String configTip : Config.tips) {
            
            tips.add(configTip);
        }
    }
}