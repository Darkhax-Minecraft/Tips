package net.darkhax.tipsmod.api.resources;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

/**
 * A serializer that is capable of reading and writing tip entries to and from JSON. New serializers can be registered
 * using {@link net.darkhax.tipsmod.api.TipsAPI#registerTipSerializer(ResourceLocation, ITipSerializer)}.
 *
 * @param <T> The type of tip entry serialized by the serializer.
 */
public interface ITipSerializer<T extends ITip> {

    /**
     * Reads a new tip entry from a JSON object.
     *
     * @param id   The ID assigned to the tip entry by the resource pack manager. This is based on where the entry file
     *             is being read from. For example a file in "assets/examplemod/tips/first_tip.json" will be given the
     *             ID of "examplemod:first_tip".
     * @param json The JSON object to read data from.
     * @return A newly deserialized tip entry.
     */
    T fromJSON(ResourceLocation id, JsonObject json);

    /**
     * Write a tip entry produced by this serializer back to JSON.
     *
     * @param toWrite The tip entry to write to JSON.
     * @return The newly written JSON object.
     */
    JsonObject toJSON(T toWrite);
}