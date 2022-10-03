package net.darkhax.tipsmod.impl.gui;

import java.io.File;

import com.google.common.base.Objects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.darkhax.bookshelf.api.Services;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TipsListScreen extends OptionsSubScreen {
    
    private TipsList list;
    private FunctionalCheckbox showDisabled;
    private ButtonTextField searchBar;
    private Button openConfigFile;
    private String lastSearch;
    
    public static Screen factory (Minecraft mc, Screen parent) {
        
        return new TipsListScreen(parent, mc.options);
    }
    
    public TipsListScreen(Screen screen, Options settings) {
        
        super(screen, settings, Component.translatable("gui.tips.list.title"));
    }
    
    @Override
    protected void init () {
        
        this.list = new TipsList(this, this.minecraft);
        this.addRenderableWidget(this.list);
        
        this.searchBar = new ButtonTextField(this.font, this.width / 2 - 150 / 2 - 50, this.height - 26, 150, 20, Component.translatable("gui.tips.list.search"));
        this.addRenderableWidget(this.searchBar);
        
        this.showDisabled = new FunctionalCheckbox(this.width / 2 - 20 / 2 + 50, this.height - 26, 20, 20, Component.translatable("gui.tips.list.show_disabled"), false, p -> this.list.refreshEntries(p, this.searchBar.getValue()));
        this.addRenderableWidget(this.showDisabled);
        
        this.openConfigFile = new Button(this.width - 70, this.height - 26, 60, 20, Component.translatable("gui.tips.list.config"), this::openConfigFile);
        this.addRenderableWidget(this.openConfigFile);
        
        this.searchBar.setResponder(s -> {
            
            if (!Objects.equal(this.lastSearch, s)) {
                this.list.refreshEntries(this.showDisabled.selected(), s);
                this.lastSearch = s;
            }
        });
        
        super.init();
    }
    
    @Override
    public void render (PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        
        this.list.render(matrixStack, mouseX, mouseY, partialTicks);
        this.searchBar.render(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 16, 0xffffff);
        
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
    
    private void openConfigFile (Button button) {

        final File cfgFile = Services.PLATFORM.getConfigPath().resolve("tips.json").toFile();
        Util.getPlatform().openFile(cfgFile);
    }
}