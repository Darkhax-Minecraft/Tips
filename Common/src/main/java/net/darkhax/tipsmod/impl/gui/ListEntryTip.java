package net.darkhax.tipsmod.impl.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.tipsmod.api.TipsAPI;
import net.darkhax.tipsmod.api.resources.ITip;
import net.darkhax.tipsmod.impl.client.RenderUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.joml.Matrix4f;

public class ListEntryTip extends ListEntry {

    final ITip tip;

    ListEntryTip(Minecraft mc, ITip tip) {

        super(mc);
        this.tip = tip;
    }

    @Override
    public void render(PoseStack matrix, int index, int yStart, int xStart, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {

        if (!TipsAPI.canDisplayTip(this.tip)) {

            drawGradient(matrix, xStart - 2, yStart + 2, width + 2, height - 6, 0x40ff0000, 0xff700000, 0xff600000);
        }

        this.mc.font.draw(matrix, this.tip.getTitle(), xStart, yStart + 2, 0xffffff);
        final Font font = this.mc.font;
        RenderUtils.renderLinesWrapped(matrix, font, xStart, yStart + 7 + font.lineHeight, font.lineHeight, 0xffffff, this.tip.getText(), width);
    }

    @Override
    public void renderMouseOver(PoseStack matrix, int mouseX, int mouseY) {

        final List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("gui.tips.list.entry.tip_id", this.tip.getId().toString()));
        tooltip.add(Component.translatable("gui.tips.list.entry.added_by", Services.PLATFORM.getModName(this.tip.getId().getNamespace())).withStyle(ChatFormatting.BLUE));

        if (!TipsAPI.canDisplayTip(this.tip)) {
            tooltip.add(Component.translatable("gui.tips.list.entry.disabled").withStyle(ChatFormatting.RED));
        }

        tooltip.add(Component.translatable("gui.tips.list.entry.cycle_time", ticksToTime((tip.getCycleTime() / 50), false, false)));

        if (this.isSelected) {
            tooltip.add(Component.translatable("gui.tips.list.entry.copied").withStyle(ChatFormatting.GREEN));
        }

        else {
            tooltip.add(Component.translatable("gui.tips.list.entry.click_to_copy").withStyle(ChatFormatting.GRAY));
        }

        this.mc.screen.renderComponentTooltip(matrix, tooltip, mouseX, mouseY);
    }

    @Override
    public void updateNarrator(NarrationElementOutput narrator) {

        narrator.add(NarratedElementType.TITLE, this.tip.getTitle());
        narrator.add(NarratedElementType.HINT, this.tip.getText());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if (this.isSelected && this.isMouseOver(mouseX, mouseY)) {

            this.mc.keyboardHandler.setClipboard(this.tip.getId().toString());
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public static void drawGradient(PoseStack mStack, int x, int y, int width, int height, int backgroundColor, int borderColorStart, int borderColorEnd) {

        RenderSystem.disableDepthTest();

        final int zLevel = 0;

        mStack.pushPose();
        final Matrix4f mat = mStack.last().pose();
        drawGradientRect(mat, zLevel, x - 3, y - 4, x + width + 3, y - 3, backgroundColor, backgroundColor);
        drawGradientRect(mat, zLevel, x - 3, y + height + 3, x + width + 3, y + height + 4, backgroundColor, backgroundColor);
        drawGradientRect(mat, zLevel, x - 3, y - 3, x + width + 3, y + height + 3, backgroundColor, backgroundColor);
        drawGradientRect(mat, zLevel, x - 4, y - 3, x - 3, y + height + 3, backgroundColor, backgroundColor);
        drawGradientRect(mat, zLevel, x + width + 3, y - 3, x + width + 4, y + height + 3, backgroundColor, backgroundColor);
        drawGradientRect(mat, zLevel, x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, borderColorStart, borderColorEnd);
        drawGradientRect(mat, zLevel, x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, borderColorStart, borderColorEnd);
        drawGradientRect(mat, zLevel, x - 3, y - 3, x + width + 3, y - 3 + 1, borderColorStart, borderColorStart);
        drawGradientRect(mat, zLevel, x - 3, y + height + 2, x + width + 3, y + height + 3, borderColorEnd, borderColorEnd);

        mStack.popPose();
        RenderSystem.enableDepthTest();
    }


    private static void drawGradientRect(Matrix4f mat, int zLevel, int left, int top, int right, int bottom, int startColor, int endColor) {

        float startAlpha = (float) (startColor >> 24 & 255) / 255.0F;
        float startRed = (float) (startColor >> 16 & 255) / 255.0F;
        float startGreen = (float) (startColor >> 8 & 255) / 255.0F;
        float startBlue = (float) (startColor & 255) / 255.0F;
        float endAlpha = (float) (endColor >> 24 & 255) / 255.0F;
        float endRed = (float) (endColor >> 16 & 255) / 255.0F;
        float endGreen = (float) (endColor >> 8 & 255) / 255.0F;
        float endBlue = (float) (endColor & 255) / 255.0F;

        drawGradientRect(mat, zLevel, left, top, right, bottom, startAlpha, startRed, startGreen, startBlue, endAlpha, endRed, endGreen, endBlue);
    }

    private static void drawGradientRect(Matrix4f mat, int zLevel, int left, int top, int right, int bottom, float startAlpha, float startRed, float startGreen, float startBlue, float endAlpha, float endRed, float endGreen, float endBlue) {

        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(mat, right, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        buffer.vertex(mat, left, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        buffer.vertex(mat, left, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        buffer.vertex(mat, right, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        tessellator.end();

        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    private static Component ticksToTime(int ticks, boolean prefix, boolean color) {

        final boolean isPositive = ticks > 0;
        ticks = Math.abs(ticks);
        int seconds = ticks / 20;
        final int minutes = seconds / 60;
        seconds = seconds % 60;

        final String result = seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
        final MutableComponent component = Component.literal(prefix ? (isPositive ? "+" : "-") + result : result);
        return color ? (isPositive ? component.withStyle(ChatFormatting.GREEN) : component.withStyle(ChatFormatting.RED)) : component;
    }
}