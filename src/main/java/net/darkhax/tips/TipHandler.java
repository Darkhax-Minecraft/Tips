package net.darkhax.tips;

import java.lang.reflect.Field;

import net.darkhax.tips.client.shader.WrappedFrameBuffer;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = TipsMod.MODID)
public class TipHandler {
    
    /**
     * A reference to the frame buffer field in LoadingScreenRenderer. This is used to modify
     * that field, since it is normally private.
     */
    private static final Field bufferField = ReflectionHelper.findField(LoadingScreenRenderer.class, "field_146588_g", "framebuffer");
    
    /**
     * The previous window width. This is used by
     * {@link #onRenderTick(net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent)}
     * to check for window resizing.
     */
    private static int prevWidth = 0;
    
    /**
     * The previous window height. This is used by
     * {@link #onRenderTick(net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent)}
     * to check for window resizing.
     */
    private static int prevHeight = 0;
    
    /**
     * This event is used to reload the current tip that is being displayed.
     */
    @SubscribeEvent
    public static void onGuiInit (InitGuiEvent event) {
        
        TipsAPI.getRandomTip();
    }
    
    /**
     * This event is used to detect when a valid loading screen GUI is open, and render a tip
     * on top of it. Note that this doesn't cover the world loading screen because Mojang
     * doesn't consider that to be a GUI.
     */
    @SubscribeEvent
    public static void onGuiDraw (DrawScreenEvent event) {
        
        final GuiScreen gui = event.getGui();
        
        if (gui instanceof GuiScreenWorking || gui instanceof GuiDownloadTerrain || gui instanceof GuiConnecting || gui instanceof GuiDisconnected) {
            
            TipsAPI.renderTip();
        }
    }
    
    /**
     * This event is used to detect window resizing, and the initial window initialization.
     * This mod uses this to insert a hook into the world loading screen renderer, which gets
     * reset every time the window is resized. This hook is needed because Mojang decided the
     * loading screen GUI isn't an actual GUI.
     */
    @SubscribeEvent
    public static void onRenderTick (TickEvent.RenderTickEvent event) {
        
        final Minecraft mc = Minecraft.getMinecraft();
        
        // Check if the window has resized, or if this is the first render tick.
        if (mc.displayWidth != prevWidth || mc.displayHeight != prevHeight) {
            
            try {
                
                // Get the old frame buffer, wrap it up, and replace it.
                final Framebuffer delegate = (Framebuffer) bufferField.get(mc.loadingScreen);
                final WrappedFrameBuffer wrapped = new WrappedFrameBuffer(delegate);
                bufferField.set(mc.loadingScreen, wrapped);
                
                // Set the display size for future resize checks.
                prevWidth = mc.displayWidth;
                prevHeight = mc.displayHeight;
                
                TipsMod.LOG.info("Window size change detected! World loading screen hooks have been reapplied.");
            }
            
            catch (final Exception e) {
                
                TipsMod.LOG.error("An error occured while trying to hook into the world loading screen!");
                TipsMod.LOG.catching(e);
            }
        }
    }
}
