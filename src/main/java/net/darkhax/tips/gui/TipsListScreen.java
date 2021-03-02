package net.darkhax.tips.gui;

import java.io.File;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLPaths;

@OnlyIn(Dist.CLIENT)
public class TipsListScreen extends SettingsScreen {
    
    private TipsList list;
    private CheckboxButton showDisabled;
    private TextFieldWidget searchBar;
    private Button openConfigFile;
    
    public static Screen factory(Minecraft mc, Screen parent) {
        
        return new TipsListScreen(parent, mc.gameSettings);
    }
    
    public TipsListScreen(Screen screen, GameSettings settings) {
        
        super(screen, settings, new TranslationTextComponent("gui.tips.list.title"));
    }
    
    @Override
    protected void init () {
        
        this.list = new TipsList(this, this.minecraft);
        this.children.add(this.list);
        
        this.searchBar = new ButtonTextField(this.font, (this.width / 2 - 150 / 2) - 50, this.height - 26, 150, 20, new TranslationTextComponent("gui.tips.list.search"));
        this.children.add(this.searchBar);
        
        this.showDisabled = new ButtonCheckbox((this.width / 2 - 20 / 2) + 50, this.height - 26, 20, 20, new TranslationTextComponent("gui.tips.list.show_disabled"), false, p -> this.list.refreshEntries(p, this.searchBar.getText()));
        this.addButton(showDisabled);
        
        this.openConfigFile = new Button(this.width - 70, this.height - 26, 60, 20, new TranslationTextComponent("gui.tips.list.config"), this::openConfigFile);
        this.addButton(this.openConfigFile);
        
        this.searchBar.setResponder(s -> list.refreshEntries(this.showDisabled.isChecked(), s));
        
        super.init();
    }
    
    @Override
    public void render (MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        
        this.list.render(matrixStack, mouseX, mouseY, partialTicks);
        this.searchBar.render(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 16, 0xffffff);
        
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
    
    private void openConfigFile(Button button) {
        
        File cfgFile = FMLPaths.CONFIGDIR.get().resolve("tips-client.toml").toFile();
        Util.getOSType().openURI(cfgFile.toURI());
    }
}