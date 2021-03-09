package net.darkhax.tips;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.darkhax.tips.data.tip.ITip;
import net.darkhax.tips.data.tip.ITipSerializer;
import net.darkhax.tips.data.tip.SimpleTip;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * The API for interacting with tips from other mods. Obtainable through {@link Tips#API}.
 */
public final class TipsAPI {
    
    /**
     * The default tip tile, used when no title is defined.
     */
    public static final ITextComponent DEFAULT_TITLE = new TranslationTextComponent("tips.title.tip").mergeStyle(TextFormatting.BOLD, TextFormatting.UNDERLINE, TextFormatting.YELLOW);
    
    /**
     * A default value used to display an error when no tips are available for display.
     */
    public static final SimpleTip NO_TIPS = new SimpleTip(new ResourceLocation("tips", "empty"), DEFAULT_TITLE, new TranslationTextComponent("tips.tip.no_tips").mergeStyle(TextFormatting.RED), -1);
    
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
        
        final Collection<ITip> tipPool = this.tips.entrySet().stream().filter(e -> Tips.CFG.canLoadTip(e.getKey())).map(e -> e.getValue()).collect(Collectors.toList());
        return tipPool.isEmpty() ? NO_TIPS : tipPool.stream().skip((int) (tipPool.size() * Math.random())).findFirst().orElse(null);
    }
    
    /**
     * Replaces the map of currently available tips.
     * 
     * @param tips The new tips to display.
     */
    public void updateTips (Map<ResourceLocation, ITip> tips) {
        
        this.tips = ImmutableMap.copyOf(tips);
    }
    
    public Map<ResourceLocation, ITip> getTips () {
        
        return this.tips;
    }
}