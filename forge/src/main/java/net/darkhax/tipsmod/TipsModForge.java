package net.darkhax.tipsmod;

import net.darkhax.tipsmod.impl.Constants;
import net.darkhax.tipsmod.impl.TipsModCommon;
import net.darkhax.tipsmod.impl.client.TipRenderHandler;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class TipsModForge {

    public TipsModForge() {

        TipsModCommon.init();
        MinecraftForge.EVENT_BUS.addListener(TipsModForge::drawScreen);
    }

    private static void drawScreen(ScreenEvent.Render.Post event) {

        TipRenderHandler.drawTip(event.getGuiGraphics(), event.getScreen());
    }
}