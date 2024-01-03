package net.darkhax.tipsmod;

import net.darkhax.tipsmod.impl.TipsModCommon;
import net.darkhax.tipsmod.impl.client.TipRenderHandler;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

public class TipsModNeoForgeClient {

    public static void init() {

        TipsModCommon.init();
        NeoForge.EVENT_BUS.addListener(TipsModNeoForgeClient::drawScreen);
    }

    private static void drawScreen(ScreenEvent.Render.Post event) {

        TipRenderHandler.drawTip(event.getGuiGraphics(), event.getScreen());
    }
}