package net.darkhax.tipsmod.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;
import net.darkhax.bookshelf.api.Services;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.darkhax.tipsmod.api.TipsAPI;
import net.minecraft.network.chat.Component;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Component.class, new ComponentTypeAdapter()).excludeFieldsWithoutExposeAnnotation().create();

    @Expose
    public int defaultCycleTime = 5000;

    @Expose
    public List<String> ignoredNamespaces = new ArrayList<>();

    @Expose
    public List<String> ignoredTips = new ArrayList<>();

    @Expose
    public boolean hideUnlocalizedTips = false;

    @Expose
    public float tipRenderWidthPercent = 0.35f;

    @Expose
    public Component defaultTitle = TipsAPI.DEFAULT_TITLE;

    @Expose
    public List<String> ignoredScreens = new ArrayList<>();

    public static Config load() {

        File configFile = Services.PLATFORM.getConfigPath().resolve("tips.json").toFile();

        Config config = new Config();

        // Attempt to load existing config file
        if (configFile.exists()) {

            try (FileReader reader = new FileReader(configFile)) {

                config = GSON.fromJson(reader, Config.class);
                Constants.LOG.info("Loaded config file.");
            }

            catch (Exception e) {

                Constants.LOG.error("Could not read config file {}. Defaults will be used.", configFile.getAbsolutePath(), e);
            }
        }

        else {

            Constants.LOG.info("Creating a new config file at {}.", configFile.getAbsolutePath());
            configFile.getParentFile().mkdirs();
        }

        try (FileWriter writer = new FileWriter(configFile)) {

            GSON.toJson(config, writer);
            Constants.LOG.info("Saved config file.");
        }

        catch (Exception e) {

            Constants.LOG.error("Could not write config file '{}'!", configFile.getAbsolutePath(), e);
        }

        return config;
    }

    private static final class ComponentTypeAdapter implements JsonSerializer<Component>, JsonDeserializer<Component> {

        @Override
        public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Serializers.TEXT.fromJSON(json);
        }

        @Override
        public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
            return Serializers.TEXT.toJSON(src);
        }
    }
}