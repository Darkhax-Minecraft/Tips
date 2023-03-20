package net.darkhax.tipsmod.impl;

import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.registry.RegistryDataProvider;
import net.darkhax.tipsmod.api.TipsAPI;
import net.darkhax.tipsmod.impl.resources.SimpleTip;
import net.darkhax.tipsmod.impl.resources.TipManager;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.ProgressScreen;

public class TipsModCommon extends RegistryDataProvider {

    public static final TipManager TIP_MANAGER = new TipManager();
    public static final Config CONFIG = Config.load();

    public static void init() {

        Services.REGISTRIES.loadContent(new TipsModCommon());
    }

    private TipsModCommon() {

        super(Constants.MOD_ID);
        this.resourceListeners.add(() -> TIP_MANAGER, "tip_loader");
        TipsAPI.registerTipSerializer(TipsAPI.DEFAULT_SERIALIZER, SimpleTip.SERIALIZER);

        TipsAPI.registerTipScreen(GenericDirtMessageScreen.class);
        TipsAPI.registerTipScreen(ConnectScreen.class);
        TipsAPI.registerTipScreen(DisconnectedScreen.class);
        TipsAPI.registerTipScreen(LevelLoadingScreen.class);
        TipsAPI.registerTipScreen(ProgressScreen.class);
        TipsAPI.registerTipScreen(PauseScreen.class);
        TipsAPI.registerTipScreen(DeathScreen.class);
    }
}