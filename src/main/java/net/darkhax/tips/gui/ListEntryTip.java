package net.darkhax.tips.gui;

import java.awt.TextComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.http.impl.NoConnectionReuseStrategy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.darkhax.bookshelf.util.RenderUtils;
import net.darkhax.tips.Tips;
import net.darkhax.tips.data.tip.ITip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.gui.GuiUtils;

@OnlyIn(Dist.CLIENT)
public class ListEntryTip extends ListEntry {
    
    final ITip tip;
    
    ListEntryTip(Minecraft mc, ITip tip) {
        
        super(mc);
        this.tip = tip;
    }
    
    @Override
    public void render (MatrixStack matrix, int index, int yStart, int xStart, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
        
        if (!Tips.CFG.canLoadTip(this.tip.getId())) {
            
            drawGradient(matrix, xStart - 2, yStart + 2, width + 2, height - 6, 0x40ff0000, 0xff700000, 0xff600000);
        }
        
        this.mc.fontRenderer.func_243248_b(matrix, this.tip.getTitle(), xStart, yStart + 2, 0xffffff);
        final FontRenderer font = this.mc.fontRenderer;
        RenderUtils.renderLinesWrapped(matrix, font, xStart, yStart + 7 + this.mc.fontRenderer.FONT_HEIGHT, font.FONT_HEIGHT, 0xffffff, this.tip.getText(), width);
    }
    
    @Override
    public void renderMouseOver (MatrixStack matrix, int mouseX, int mouseY) {
        
        final List<ITextComponent> tooltip = new ArrayList<>();
        tooltip.add(new TranslationTextComponent("gui.tips.list.entry.tip_id", this.tip.getId().toString()));
        tooltip.add(new TranslationTextComponent("gui.tips.list.entry.added_by", getModName(this.tip.getId().getNamespace())).mergeStyle(TextFormatting.BLUE));
        
        if (!Tips.CFG.canLoadTip(this.tip.getId())) {
            tooltip.add(new TranslationTextComponent("gui.tips.list.entry.disabled").mergeStyle(TextFormatting.RED));
        }
        
        tooltip.add(new TranslationTextComponent("gui.tips.list.entry.cycle_time", ticksToTime((tip.getCycleTime() / 50), false, false)));
        
        if (this.isSelected) {
            tooltip.add(new TranslationTextComponent("gui.tips.list.entry.copied").mergeStyle(TextFormatting.GREEN));
        }
        
        else {
            tooltip.add(new TranslationTextComponent("gui.tips.list.entry.click_to_copy").mergeStyle(TextFormatting.GRAY));
        }
        
        this.mc.currentScreen.func_243308_b(matrix, tooltip, mouseX, mouseY);
    }
    
    @Override
    public boolean mouseClicked (double mouseX, double mouseY, int button) {
        
        if (this.isSelected && this.isMouseOver(mouseX, mouseY)) {
            
            this.mc.keyboardListener.setClipboardString(this.tip.getId().toString());
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    private static String getModName (String modId) {
        
        if (ModList.get().isLoaded(modId)) {
            
            final Optional<? extends ModContainer> modInfo = ModList.get().getModContainerById(modId);
            
            if (modInfo.isPresent()) {
                
                return modInfo.get().getModInfo().getDisplayName();
            }
        }
        
        return modId;
    }
    
    public static void drawGradient (MatrixStack mStack, int x, int y, int width, int height, int backgroundColor, int borderColorStart, int borderColorEnd) {
        
        RenderSystem.disableRescaleNormal();
        RenderSystem.disableDepthTest();
        
        final int zLevel = 0;
        
        mStack.push();
        final Matrix4f mat = mStack.getLast().getMatrix();
        GuiUtils.drawGradientRect(mat, zLevel, x - 3, y - 4, x + width + 3, y - 3, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(mat, zLevel, x - 3, y + height + 3, x + width + 3, y + height + 4, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(mat, zLevel, x - 3, y - 3, x + width + 3, y + height + 3, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(mat, zLevel, x - 4, y - 3, x - 3, y + height + 3, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(mat, zLevel, x + width + 3, y - 3, x + width + 4, y + height + 3, backgroundColor, backgroundColor);
        GuiUtils.drawGradientRect(mat, zLevel, x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, borderColorStart, borderColorEnd);
        GuiUtils.drawGradientRect(mat, zLevel, x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, borderColorStart, borderColorEnd);
        GuiUtils.drawGradientRect(mat, zLevel, x - 3, y - 3, x + width + 3, y - 3 + 1, borderColorStart, borderColorStart);
        GuiUtils.drawGradientRect(mat, zLevel, x - 3, y + height + 2, x + width + 3, y + height + 3, borderColorEnd, borderColorEnd);
        
        mStack.pop();
        RenderSystem.enableDepthTest();
        RenderSystem.enableRescaleNormal();
    }
    
    private static ITextComponent ticksToTime (int ticks, boolean prefix, boolean color) {
        
        final boolean isPositive = ticks > 0;
        ticks = Math.abs(ticks);
        int seconds = ticks / 20;
        final int minutes = seconds / 60;
        seconds = seconds % 60;
        
        final String result = seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;        
        final StringTextComponent component = new StringTextComponent(prefix ? (isPositive ? "+" : "-") + result : result);
        return color ? (isPositive ? component.mergeStyle(TextFormatting.GREEN) : component.mergeStyle(TextFormatting.RED)) : component;
    }
}