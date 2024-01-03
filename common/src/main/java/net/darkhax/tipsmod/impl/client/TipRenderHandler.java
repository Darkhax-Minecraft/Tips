package net.darkhax.tipsmod.impl.client;

import net.darkhax.bookshelf.api.util.RenderHelper;
import net.darkhax.tipsmod.api.TipsAPI;
import net.darkhax.tipsmod.impl.Constants;
import net.darkhax.tipsmod.impl.resources.TipManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;

public class TipRenderHandler {

    private static long initTime = System.currentTimeMillis();
    private static TipManager.TipHolder tipHolder;

    private static void setTip(TipManager.TipHolder newTip) {

        tipHolder = newTip;
        initTime = System.currentTimeMillis();
    }

    public static void drawTip(GuiGraphics graphics, Screen parentScreen) {

        if (tipHolder == null) {

            setTip(TipsAPI.getRandomTip());
        }

        if (TipsAPI.canRenderOnScreen(parentScreen)) {

            final long currentTime = System.currentTimeMillis();
            final int currentCycleTime = tipHolder.tip().getCycleTime();

            if (currentTime - initTime > currentCycleTime) {

                setTip(TipsAPI.getRandomTip());

                if (tipHolder != null) {

                    Constants.LOG.debug("Displaying tip {} on screen {}.", tipHolder.id(), parentScreen.getClass().getSimpleName());
                }
            }

            if (tipHolder != null) {

                final int textWidth = Mth.floor(parentScreen.width * 0.35f);
                int height = parentScreen.height - 10;
                height -= RenderHelper.renderLinesReversed(graphics, 10, height, tipHolder.tip().getText(), textWidth);
                height -= 3; // padding for title
                RenderHelper.renderLinesReversed(graphics, 10, height, tipHolder.tip().getTitle(), textWidth);
            }
        }
    }
}