package net.darkhax.tips.data.tip;

import net.minecraft.util.text.ITextComponent;

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
    ITextComponent getTitle ();
    
    /**
     * Gets the body of the tip. This is the part below the title.
     * 
     * @return The body of the tip.
     */
    ITextComponent getText ();
}