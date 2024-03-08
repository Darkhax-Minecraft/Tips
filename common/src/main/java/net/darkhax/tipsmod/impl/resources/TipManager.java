package net.darkhax.tipsmod.impl.resources;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.darkhax.tipsmod.api.TipTypes;
import net.darkhax.tipsmod.api.TipsAPI;
import net.darkhax.tipsmod.api.resources.ITip;
import net.darkhax.tipsmod.impl.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TipManager extends SimpleJsonResourceReloadListener {

    private final Map<ResourceLocation, TipHolder> loadedTips = new HashMap();
    private final List<TipHolder> randomAccess = new ArrayList<>();
    private final List<TipHolder> immutableAccess = Collections.unmodifiableList(randomAccess);

    public TipManager() {

        super(new Gson(), "tips");
    }

    public List<TipHolder> getTips() {

        return this.immutableAccess;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {

        this.loadedTips.clear();
        this.randomAccess.clear();

        final long startTime = System.nanoTime();

        map.forEach((tipId, tipData) -> {

            try {

                final ITip tipEntry = TipTypes.TIP_DISPATCH.decode(JsonOps.INSTANCE, tipData).getOrThrow(false, error -> Constants.LOG.error("Could not decode tip {}. Error: {}", tipId, error)).getFirst();

                if (tipEntry != null) {

                    final TipHolder holder = new TipHolder(tipId, tipEntry);
                    this.loadedTips.put(tipId, holder);
                    this.randomAccess.add(holder);
                }

                else {
                    Constants.LOG.error("Tip {} was null and will not be loaded. Data: {}", tipId, tipData);
                }
            }

            catch (Exception e) {

                Constants.LOG.error("Failed to load tip {}!", tipId, e);
            }
        });

        Constants.LOG.info("Loaded {} tips. Took {}ms.", this.loadedTips.size(), (double) (System.nanoTime() - startTime) / 1000000d);
        Constants.LOG.info("The following screens have been registered to the tips mod.");
        TipsAPI.getTipsScreens().forEach(screen -> Constants.LOG.info("Screen: '{}' Enabled: '{}'", screen.getCanonicalName(), TipsAPI.canRenderOnScreen(screen)));
    }

    public static record TipHolder(ResourceLocation id, ITip tip) {

    }
}