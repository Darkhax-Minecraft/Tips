package net.darkhax.tips;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.darkhax.tips.data.tip.ITip;
import net.darkhax.tips.data.tip.ITipSerializer;
import net.minecraft.util.ResourceLocation;

/**
 * The API for interacting with tips from other mods. Obtainable through {@link Tips#API}.
 */
public final class TipsAPI {
    
    /**
     * A registry of tip serializers.
     */
    private final Map<ResourceLocation, ITipSerializer<?>> tipSerializers = new HashMap<>();
    
    /**
     * Registers a new type of tip serializer.
     * 
     * @param id The ID for the tip serializer.
     * @param serializer The tip serializer.
     */
    public void registerTipSerializer (ResourceLocation id, ITipSerializer<?> serializer) {
        
        this.tipSerializers.put(id, serializer);
    }
    
    /**
     * Gets a tip serializer by it's ID.
     * 
     * @param id The ID of the serializer.
     * @return The tip serializer or null if none were found.
     */
    @Nullable
    public ITipSerializer<?> getTipSerializer (ResourceLocation id) {
        
        return this.tipSerializers.get(id);
    }
    
    /**
     * A map of currently loaded tips.
     */
    private Map<ResourceLocation, ITip> tips = new HashMap<>();
    
    /**
     * Gets a tip by it's ID.
     * 
     * @param id The ID of the tip to get.
     * @return The tip that was found, or null if it does not exist.
     */
    @Nullable
    public ITip getTip (ResourceLocation id) {
        
        return this.tips.get(id);
    }
    
    /**
     * Gets a random tip from the currently loaded tips.
     * 
     * @return A random tip.
     */
    @Nullable
    public ITip getRandomTip () {
        
        final Collection<ITip> tipPool = this.tips.values();
        return tipPool.stream().skip((int) (tipPool.size() * Math.random())).findFirst().orElse(null);
    }
    
    /**
     * Replaces the map of currently available tips.
     * 
     * @param tips The new tips to display.
     */
    public void updateTips (Map<ResourceLocation, ITip> tips) {
        
        this.tips = ImmutableMap.copyOf(tips);
    }
}