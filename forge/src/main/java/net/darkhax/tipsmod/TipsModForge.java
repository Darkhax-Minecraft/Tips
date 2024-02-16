package net.darkhax.tipsmod;

import net.darkhax.tipsmod.impl.Constants;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class TipsModForge {

    public TipsModForge() {

        TipsModForgeClient.init();
    }
}