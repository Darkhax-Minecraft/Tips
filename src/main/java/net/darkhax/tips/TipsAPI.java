package net.darkhax.tips;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import net.darkhax.tips.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class TipsAPI {
    
    /**
     * A list of all tip adders. A string list consumer is used because the tip list can be
     * loaded and built many times.
     */
    private static final List<Consumer<List<String>>> tipAdders = new ArrayList<>();
    
    /**
     * A list of all tip removers. These are invoked after all the tip adders have been invoked
     * and the initial tip list has been constructed.
     * 
     * Java Tip: If you want to remove entries while looping over the list you should use an
     * iterator to remove the tips to avoid concurrent modification exceptions!
     */
    private static final List<Consumer<List<String>>> tipRemovers = new ArrayList<>();
    
    /**
     * An internal constant source of random. This is used internally to randomly select the
     * tip to display.
     */
    private static final Random RANDOM = new Random();
    
    /**
     * The list of currently loaded tips. This list is rebuilt occasionally using
     * {@link #reloadTips()}.
     */
    private static List<String> loadedtips = new ArrayList<>();
    
    /**
     * The current tip that should be displayed. This is determined by {@link #getRandomTip()}.
     */
    private static String currentTip;
    
    /**
     * Adds a tip adder to the list of all tip adder. Tips can be reloaded, which is why a
     * string list consumer is required.
     * 
     * @param adder A functional interface which accepts and modifies the tip list.
     * @return Whether or not it was added successfully.
     */
    public static boolean addTips (Consumer<List<String>> adder) {
        
        return tipAdders.add(adder);
    }
    
    /**
     * Adds a tip remover to the list of all tip removers. Tips can be reloaded, which is why a
     * string list consumer is required. Tip removers are applied after the initial tip list
     * has been created and allows for tips to be removed.
     * 
     * @param remover A functional interface which accepts and modifies the tip list.
     * @return Whether or not it was added successfully.
     */
    public static boolean removetips (Consumer<List<String>> remover) {
        
        return tipRemovers.add(remover);
    }
    
    /**
     * Generates a new random tip from {@link #loadedtips}. This will also set the value of
     * {@link #currentTip} which is used to render the tips on loading screens.
     * 
     * @return A random tip from the loaded tips list. If there are no tips you will get an
     *         error tip.
     */
    public static String getRandomTip () {
        
        currentTip = loadedtips.isEmpty() ? TextFormatting.RED + I18n.format("tips.gui.error") : loadedtips.get(RANDOM.nextInt(loadedtips.size()));
        return currentTip;
    }
    
    /**
     * Clears out the list of loaded tips and rebuilds it using the registered tip adders and
     * removers. Reloads can happen in many situations such as texture pack change, config
     * change, resource reload, and so on.
     */
    public static void reloadTips () {
        
        final long startTime = System.currentTimeMillis();
        
        loadedtips.clear();
        
        // Populate the tip list using the registered tip adders.
        for (Consumer<List<String>> adder : tipAdders) {
            
            adder.accept(loadedtips);
        }
        
        // Remove undesired tips using registered tip removers.
        for (Consumer<List<String>> remover : tipRemovers) {
            
            remover.accept(loadedtips);
        }
        
        TipsMod.LOG.info("Tips have been reloaded. Loaded {} tips. Took {}ms.", loadedtips.size(), System.currentTimeMillis() - startTime);
    }
    
    /**
     * Contains the code to render a tip on the screen. Positions and color are taken from the
     * configuration file. The tip rendered is taken from {@link #currentTip}. This method can
     * also be used by mods to add tips to their own GUIs.
     */
    public static void renderTip () {
        
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        final ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        
        String[] tip = currentTip.split("#SPLIT#");
        fontRenderer.drawString(TextFormatting.BOLD.toString() + (tip.length == 2 ? tip[1] : I18n.format("tips.gui.title")), Config.xOffset, resolution.getScaledHeight() - Config.yOffset, Config.titleColor);
        fontRenderer.drawSplitString(tip[0], Config.xOffset, resolution.getScaledHeight() - Config.yOffset + fontRenderer.FONT_HEIGHT, resolution.getScaledWidth() / 2, Config.textColor);
    }
}