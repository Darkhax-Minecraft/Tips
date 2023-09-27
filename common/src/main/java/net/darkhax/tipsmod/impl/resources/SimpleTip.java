package net.darkhax.tipsmod.impl.resources;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.darkhax.tipsmod.api.TipsAPI;
import net.darkhax.tipsmod.api.resources.ITip;
import net.darkhax.tipsmod.api.resources.ITipSerializer;
import net.darkhax.tipsmod.impl.TipsModCommon;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/**
 * A simple implementation of the tip.
 */
public class SimpleTip implements ITip {

    /**
     * The serializer for this type of tip.
     */
    public static final ITipSerializer<SimpleTip> SERIALIZER = new Serializer();

    /**
     * The namespaced id of the tip.
     */
    private final ResourceLocation id;

    /**
     * The title text to display.
     */
    private final Component title;

    /**
     * The body of the tip.
     */
    private final Component text;

    /**
     * Time to keep the tip displayed for.
     */
    private final Optional<Integer> cycleTime;

    public SimpleTip(ResourceLocation id, Component title, Component text, Optional<Integer> cycleTime) {

        this.id = id;
        this.title = title;
        this.text = text;
        this.cycleTime = cycleTime;
    }

    @Override
    public ResourceLocation getId() {

        return this.id;
    }

    @Override
    public Component getTitle() {

        return this.title;
    }

    @Override
    public Component getText() {

        return this.text;
    }

    @Override
    public int getCycleTime() {

        return this.cycleTime.orElse(TipsModCommon.CONFIG.defaultCycleTime);
    }

    private static final class Serializer implements ITipSerializer<SimpleTip> {

        @Override
        public SimpleTip fromJSON(ResourceLocation id, JsonObject json) {

            final Component title = Serializers.TEXT.fromJSON(json, "title", TipsAPI.DEFAULT_TITLE);
            final Component text = Serializers.TEXT.fromJSON(json, "tip");
            final Optional<Integer> cycleTime = Serializers.INT.fromJSONOptional(json, "cycleTime");

            if (title == null) {

                throw new JsonParseException("Tip " + id.toString() + " does not have a title. This is required!");
            }

            if (text == null) {

                throw new JsonParseException("Tip " + id.toString() + " does not have text. This is required.");
            }

            return new SimpleTip(id, title, text, cycleTime);
        }

        @Override
        public JsonObject toJSON(SimpleTip toWrite) {

            final JsonObject json = new JsonObject();
            Serializers.RESOURCE_LOCATION.toJSON(json, "type", TipsAPI.DEFAULT_SERIALIZER);
            Serializers.TEXT.toJSON(json, "title", toWrite.title);
            Serializers.TEXT.toJSON(json, "tip", toWrite.text);
            Serializers.INT.toJSONOptional(json, "cycleTime", toWrite.cycleTime);
            return json;
        }
    }
}