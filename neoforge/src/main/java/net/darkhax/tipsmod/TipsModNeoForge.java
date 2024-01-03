package net.darkhax.tipsmod;

import net.darkhax.bookshelf.api.Services;
import net.darkhax.tipsmod.impl.Constants;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class TipsModNeoForge {

    public TipsModNeoForge() {

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> IExtensionPoint.DisplayTest.IGNORESERVERONLY, (a, b) -> true));

        if (Services.PLATFORM.isPhysicalClient()) {

            TipsModNeoForgeClient.init();
        }
    }
}