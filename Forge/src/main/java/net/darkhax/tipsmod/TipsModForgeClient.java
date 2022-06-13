package net.darkhax.tipsmod;

import net.darkhax.tipsmod.impl.TipsModCommon;
import net.darkhax.tipsmod.impl.gui.TipsListScreen;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.client.event.ScreenEvent;
import net.darkhax.tipsmod.impl.client.TipRenderHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;

public class TipsModForgeClient {

    public static void init() {

        TipsModCommon.init();
        MinecraftForge.EVENT_BUS.addListener(TipsModForgeClient::drawScreen);
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory(TipsListScreen::factory));
    }

    private static void drawScreen(ScreenEvent.DrawScreenEvent event) {

        TipRenderHandler.drawTip(event.getPoseStack(), event.getScreen());
    }
}