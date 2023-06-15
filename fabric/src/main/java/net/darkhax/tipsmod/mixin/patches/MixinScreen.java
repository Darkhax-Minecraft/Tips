package net.darkhax.tipsmod.mixin.patches;

import net.darkhax.tipsmod.impl.client.TipRenderHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class MixinScreen {

    @Inject(method = "renderWithTooltip(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("RETURN"))
    private void renderWithTooltip(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo cbi) {

        TipRenderHandler.drawTip(graphics, (Screen) ((Object) this));
    }
}
