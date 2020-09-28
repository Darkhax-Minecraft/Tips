package net.darkhax.tips.data.tip;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.darkhax.tips.TipsAPI;

/**
 * A serializer that can read tips to and from json. These can be registered by calling
 * {@link TipsAPI#registerTipSerializer(net.minecraft.util.ResourceLocation, ITipSerializer)}
 * during the FMLClientSetupEvent.
 *
 * @param <T> The type of tip to read and write.
 */
public interface ITipSerializer<T extends ITip> {
    
    /**
     * Reads a tip from a json tree.
     * 
     * @param json The json tree.
     * @return The tip that was read.
     */
    T read (JsonObject json);
    
    /**
     * Writes a tip to a json tree.
     * 
     * @param toWrite The tip to write.
     * @return The json tree for the tip.
     */
    JsonElement write (T toWrite);
}