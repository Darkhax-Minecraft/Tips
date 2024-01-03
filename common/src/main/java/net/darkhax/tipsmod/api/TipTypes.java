package net.darkhax.tipsmod.api;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.darkhax.bookshelf.api.data.bytebuf.BookshelfByteBufs;
import net.darkhax.bookshelf.api.data.bytebuf.ByteBufHelper;
import net.darkhax.bookshelf.api.data.codecs.BookshelfCodecs;
import net.darkhax.bookshelf.api.data.codecs.CodecHelper;
import net.darkhax.tipsmod.api.resources.ITip;
import net.darkhax.tipsmod.impl.Constants;
import net.darkhax.tipsmod.impl.resources.SimpleTip;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class TipTypes {

    private static final Map<ResourceLocation, TipType<? extends ITip>> TIP_TYPES = new HashMap<>();
    public static final CodecHelper<TipType<? extends ITip>> TIP_TYPE_CODEC = new CodecHelper<>(ResourceLocation.CODEC.xmap(id -> TIP_TYPES.containsKey(id) ? TIP_TYPES.get(id) : TIP_TYPES.get(TipsAPI.DEFAULT_SERIALIZER), TipType::id));
    public static final ByteBufHelper<TipType<?>> TIP_TYPE_BUFFER = new ByteBufHelper<>(TipType::readFromBuffer, TipType::writeToBuffer);

    public static final Codec<ITip> TIP_DISPATCH = BookshelfCodecs.dispatchFallback(TIP_TYPE_CODEC.get(), ITip::getType, tipType -> (Codec<ITip>) tipType.getCodec(), () -> (Codec<ITip>) TIP_TYPES.get(TipsAPI.DEFAULT_SERIALIZER).codec.get());
    public static final CodecHelper<ITip> DISPLAY_STATE_CODEC = new CodecHelper<>(TIP_DISPATCH);
    public static final ByteBufHelper<ITip> DISPLAY_STATE_BUFFER = new ByteBufHelper<>(TipTypes::readFromBuffer, TipTypes::writeToBuffer);

    public static final TipType<SimpleTip> SIMPLE_TIP_TYPE = register(TipsAPI.DEFAULT_SERIALIZER, SimpleTip.CODEC, SimpleTip.BUFFER);

    public static <T extends ITip> TipType<T> register(ResourceLocation id, CodecHelper<T> codec, ByteBufHelper<T> buffer) {

        final TipType<T> type = new TipType<>(id, codec, buffer);

        if (TIP_TYPES.containsKey(id)) {

            Constants.LOG.warn("Display type ID {} has already been assigned to {}. Replacing with {}.", id, TIP_TYPES.get(id), type);
        }

        TIP_TYPES.put(id, type);
        return type;
    }

    private static ITip readFromBuffer(FriendlyByteBuf buffer) {

        final TipType<?> type = TIP_TYPE_BUFFER.read(buffer);
        return type.buffer.read(buffer);
    }

    private static void writeToBuffer(FriendlyByteBuf buffer, ITip toWrite) {

        TIP_TYPE_BUFFER.write(buffer, toWrite.getType());
        toWrite.getType().buffer.write(buffer, toWrite);
    }

    public record TipType<T extends ITip>(ResourceLocation id, CodecHelper<T> codec, ByteBufHelper<T> buffer) {

        @Override
        public String toString() {

            return "TipType{id=" + id + ", codec=" + codec + ", buffer=" + buffer + '}';
        }

        public Codec<T> getCodec() {

            return this.codec.get();
        }

        private static TipType<?> readFromBuffer(FriendlyByteBuf buffer) {

            final ResourceLocation id = BookshelfByteBufs.RESOURCE_LOCATION.read(buffer);
            return TIP_TYPES.get(id);
        }

        private static void writeToBuffer(FriendlyByteBuf buffer, TipType<?> toWrite) {

            BookshelfByteBufs.RESOURCE_LOCATION.write(buffer, toWrite.id);
        }
    }
}