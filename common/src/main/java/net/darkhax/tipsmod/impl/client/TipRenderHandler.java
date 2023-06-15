package net.darkhax.tipsmod.impl.client;

import net.darkhax.bookshelf.api.util.RenderHelper;
import net.darkhax.tipsmod.api.TipsAPI;
import net.darkhax.tipsmod.api.resources.ITip;
import net.darkhax.tipsmod.impl.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

public class TipRenderHandler {

    private static long initTime = System.currentTimeMillis();
    private static ITip tip;

    private static void setTip(ITip newTip) {

        tip = newTip;
        initTime = System.currentTimeMillis();
    }

    public static void drawTip(GuiGraphics graphics, Screen parentScreen) {

        if (tip == null) {

            setTip(TipsAPI.getRandomTip());
        }

        if (TipsAPI.canRenderOnScreen(parentScreen)) {

            final long currentTime = System.currentTimeMillis();
            final int currentCycleTime = tip.getCycleTime();

            if (currentTime - initTime > currentCycleTime) {

                setTip(TipsAPI.getRandomTip());

                if (tip != null) {

                    Constants.LOG.debug("Displaying tip {} on screen {}.", tip.getId(), parentScreen.getClass().getSimpleName());
                }
            }

            if (tip != null) {

                final int textWidth = Mth.floor(parentScreen.width * 0.35f);
                int height = parentScreen.height - 10;
                height -= RenderHelper.renderLinesReversed(graphics, 10, height, tip.getText(), textWidth);
                height -= 3; // padding for title
                RenderHelper.renderLinesReversed(graphics, 10, height, tip.getTitle(), textWidth);
            }
        }
    }
}