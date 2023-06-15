package net.darkhax.tipsmod;

import net.darkhax.tipsmod.impl.TipsModCommon;
import net.fabricmc.api.ClientModInitializer;

public class TipsModFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        TipsModCommon.init();
    }
}