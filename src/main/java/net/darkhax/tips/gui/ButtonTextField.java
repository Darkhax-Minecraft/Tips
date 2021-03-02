package net.darkhax.tips.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.ITextComponent;

public class ButtonTextField extends TextFieldWidget {
    
    private final FontRenderer font;
    
    public ButtonTextField(FontRenderer font, int x, int y, int width, int height, ITextComponent title) {
        
        super(font, x, y, width, height, title);
        this.font = font;
    }
    
    @Override
    public void renderButton (MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        
        if (this.getVisible() && this.getText().isEmpty() && !this.isFocused()) {
            
            drawString(matrixStack, this.font, this.getMessage(), this.x + 4 - 1, this.y + (this.height - 8) / 2, -8355712);
        }
    }
}