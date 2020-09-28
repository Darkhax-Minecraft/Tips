package net.darkhax.tips;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.loading.FMLPaths;

public class Configuration {
    
    private final ForgeConfigSpec spec;
    
    private final ConfigValue<Integer> cycleTime;
    private final ConfigValue<List<? extends String>> removedTips;
    private final ConfigValue<List<? extends String>> removedNamespaces;
    
    public Configuration() {
        
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        builder.comment("The amount of time to wait before cycling the displayed tip. This is in miliseconds. 1000ms = 1s");
        this.cycleTime = builder.define("cycleTime", 5 * 1000);
        
        builder.comment("A list of tip IDs to remove from the list. Restart is required for changes to take effect.");
        this.removedTips = builder.defineList("removedTips", new ArrayList<String>(), s -> ResourceLocation.isResouceNameValid((String) s));
        
        builder.comment("A list of tip namespaces to remove from the list. Restart is requird for changes to take effect.");
        this.removedNamespaces = builder.defineList("removedNamespaces", new ArrayList<String>(), s -> ResourceLocation.isResouceNameValid((String) s));
        
        this.spec = builder.build();
        
        this.save();
    }
    
    public int getCycleTime () {
        
        return this.cycleTime.get();
    }
    
    public boolean canLoadTip (ResourceLocation tipId) {
        
        return !this.removedNamespaces.get().contains(tipId.getNamespace()) && !this.removedTips.get().contains(tipId.toString());
    }
    
    private void save () {
        
        final ModConfig modConfig = new ModConfig(Type.CLIENT, this.spec, ModLoadingContext.get().getActiveContainer());
        final CommentedFileConfig configData = modConfig.getHandler().reader(FMLPaths.CONFIGDIR.relative()).apply(modConfig);
        final Method setConfigDataMethod = ObfuscationReflectionHelper.findMethod(ModConfig.class, "setConfigData", CommentedConfig.class);
        
        try {
            setConfigDataMethod.invoke(modConfig, configData);
        }
        
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Tips.LOG.error("Forge's config code could not be accessed.", e);
            throw new IllegalStateException(e);
        }
        
        modConfig.save();
    }
}