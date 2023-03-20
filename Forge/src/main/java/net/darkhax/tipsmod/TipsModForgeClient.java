package net.darkhax.tipsmod;

import net.darkhax.tipsmod.impl.TipsModCommon;
import net.darkhax.tipsmod.impl.client.TipRenderHandler;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;

public class TipsModForgeClient {

    public static void init() {

        TipsModCommon.init();
        MinecraftForge.EVENT_BUS.addListener(TipsModForgeClient::drawScreen);
    }

    private static void drawScreen(ScreenEvent.Render.Post event) {

        TipRenderHandler.drawTip(event.getPoseStack(), event.getScreen());
    }
}