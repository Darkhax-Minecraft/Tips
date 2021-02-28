package net.darkhax.tips;

import net.darkhax.bookshelf.util.RenderUtils;
import net.darkhax.tips.data.tip.ITip;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.client.gui.screen.DirtMessageScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.client.gui.screen.WorldLoadProgressScreen;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Tips.MOD_ID, value = Dist.CLIENT)
public class TipRenderHandler {
    
    private static long initTime = System.currentTimeMillis();
    private static ITip tip;
    
    @SubscribeEvent
    public static void onGuiDraw (DrawScreenEvent event) {
        
        final Screen screen = event.getGui();
        
        if (screen instanceof DirtMessageScreen || screen instanceof ConnectingScreen || screen instanceof DisconnectedScreen || screen instanceof WorldLoadProgressScreen || screen instanceof WorkingScreen || screen instanceof IngameMenuScreen) {
            
            final long currentTime = System.currentTimeMillis();
            
            if (currentTime - initTime > Tips.CFG.getCycleTime()) {
                
                tip = Tips.API.getRandomTip();
                initTime = currentTime;
                
                if (tip != null) {
                    
                    Tips.LOG.debug("Displaying tip {} on screen {}.", tip.getId(), screen.getClass().getSimpleName());
                }
            }
            
            if (tip != null) {
                
                final int textWidth = MathHelper.floor(screen.width * 0.35f);
                int height = screen.height - 10;
                height -= RenderUtils.renderLinesReversed(event.getMatrixStack(), 10, height, tip.getText(), textWidth);
                height -= 3; // padding for title
                RenderUtils.renderLinesReversed(event.getMatrixStack(), 10, height, tip.getTitle(), textWidth);
            }
        }
    }
}
