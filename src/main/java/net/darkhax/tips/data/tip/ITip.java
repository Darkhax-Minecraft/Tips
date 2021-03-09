package net.darkhax.tips.data.tip;

import net.darkhax.tips.Tips;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * Defines a tip that is displayed on certain menus.
 */
public interface ITip {
    
    /**
     * Gets the namespaced identifier for the tip.
     * 
     * @return The namespaced identifier for the tip.
     */
    ResourceLocation getId ();
    
    /**
     * Gets the title for the tip. This is the part on top of the tip which explains what it
     * is.
     * 
     * @return The title of the tip.
     */
    ITextComponent getTitle ();
    
    /**
     * Gets the body of the tip. This is the part below the title.
     * 
     * @return The body of the tip.
     */
    ITextComponent getText ();
    
    /**
     * Gets the amount of time until the next tip can be displayed. By default this will be the
     * config defined cycle time.
     * 
     * @return The amount of time in ticks until the next tip will be displayed.
     */
    default int getCycleTime () {
        
        return Tips.CFG.getCycleTime();
    }
}