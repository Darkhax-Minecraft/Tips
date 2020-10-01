package net.darkhax.tips;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.tips.data.tip.SimpleTip;
import net.darkhax.tips.data.tip.TipReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;

@Mod(Tips.MOD_ID)
public class Tips {
    
    public static final String MOD_ID = "tips";
    public static final String MOD_NAME = "Tips";
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);
    
    public static TipsAPI API = new TipsAPI();
    public static Configuration config;
    
    public Tips() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        if (FMLEnvironment.dist.isClient()) {
            
            Tips.initClient();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void initClient () {
        
        config = new Configuration();
        API.registerTipSerializer(SimpleTip.TYPE_ID, SimpleTip.SERIALIZER);
        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(new TipReloadListener());
    }
}