package net.darkhax.tipsmod.impl.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class ButtonTextField extends EditBox {
    
    private final Font font;
    
    public ButtonTextField(Font font, int x, int y, int width, int height, Component title) {

        super(font, x, y, width, height, title);
        this.font = font;
    }
    
    @Override
    public void renderButton (PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        
        if (this.isVisible() && this.getValue().isEmpty() && !this.isFocused()) {
            
            drawString(matrixStack, this.font, this.getMessage(), this.getX() + 4 - 1, this.getY() + (this.height - 8) / 2, -8355712);
        }
    }
}