package net.darkhax.tipsmod.mixin.patches;

import com.mojang.blaze3d.vertex.PoseStack;
import net.darkhax.tipsmod.impl.client.TipRenderHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class MixinScreen {

    @Inject(method = "renderWithTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V", at = @At("RETURN"))
    private void renderWithTooltip(PoseStack pose, int mouseX, int mouseY, float delta, CallbackInfo cbi) {

        TipRenderHandler.drawTip(pose, (Screen) ((Object) this));
    }
}
