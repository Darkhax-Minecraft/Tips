package net.darkhax.tipsmod.impl.resources;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.darkhax.tipsmod.impl.Constants;
import net.darkhax.tipsmod.api.TipsAPI;
import net.darkhax.tipsmod.api.resources.ITip;
import net.darkhax.tipsmod.api.resources.ITipSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.client.resources.language.LanguageInfo;
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

    private final Map<ResourceLocation, ITip> loadedTips = new HashMap();
    private final List<ITip> randomAccess = new ArrayList<>();
    private final List<ITip> immutableAccess = Collections.unmodifiableList(randomAccess);

    private ClientLanguage selectedLanguage;

    public TipManager() {

        super(new Gson(), "tips");
    }

    public List<ITip> getTips() {

        return this.immutableAccess;
    }

    public ClientLanguage getSelectedLanguage() {
        return this.selectedLanguage;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {

        this.loadedTips.clear();
        this.randomAccess.clear();

        final LanguageInfo selectedLanguageInfo = Minecraft.getInstance().getLanguageManager().getSelected();
        this.selectedLanguage = ClientLanguage.loadFrom(resourceManager, Collections.singletonList(selectedLanguageInfo));

        final long startTime = System.nanoTime();

        map.forEach((tipId, tipData) -> {

            try {

                if (tipData instanceof JsonObject json) {

                    final ResourceLocation serializerID = Serializers.RESOURCE_LOCATION.fromJSON(json, "type", TipsAPI.DEFAULT_SERIALIZER);
                    final ITipSerializer<?> serializer = TipsAPI.getTipSerializer(serializerID);

                    if (serializer == null) {

                        throw new JsonParseException("Serializer " + serializerID + " is unknown!");
                    }

                    final ITip tipEntry = serializer.fromJSON(tipId, json);

                    if (tipEntry == null) {

                        throw new JsonParseException("Serializer " + serializerID + " produced a null result!");
                    }

                    this.loadedTips.put(tipId, tipEntry);
                    this.randomAccess.add(tipEntry);
                }
            }

            catch (Exception e) {

                Constants.LOG.error("Failed to load tip {}!", tipId, e);
            }
        });

        Constants.LOG.debug("Loaded {} tips. Took {}ms.", this.loadedTips.size(), (double) (System.nanoTime() - startTime) / 1000000d);
    }
}