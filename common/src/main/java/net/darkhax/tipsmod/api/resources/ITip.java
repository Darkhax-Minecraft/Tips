package net.darkhax.tipsmod.api.resources;

import net.darkhax.tipsmod.api.TipTypes;
import net.darkhax.tipsmod.impl.TipsModCommon;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Defines a tip that is displayed on certain menus.
 */
public interface ITip {
    
    /**
     * Gets the title for the tip. This is the part on top of the tip which explains what it
     * is.
     * 
     * @return The title of the tip.
     */
    Component getTitle ();
    
    /**
     * Gets the body of the tip. This is the part below the title.
     * 
     * @return The body of the tip.
     */
    Component getText ();
    
    /**
     * Gets the amount of time until the next tip can be displayed. By default this will be the
     * config defined cycle time.
     * 
     * @return The amount of time in ticks until the next tip will be displayed.
     */
    default int getCycleTime () {

        return TipsModCommon.CONFIG.defaultCycleTime;
    }

    TipTypes.TipType getType();
}