package net.darkhax.tipsmod.impl.resources;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.darkhax.bookshelf.api.data.bytebuf.BookshelfByteBufs;
import net.darkhax.bookshelf.api.data.bytebuf.ByteBufHelper;
import net.darkhax.bookshelf.api.data.codecs.BookshelfCodecs;
import net.darkhax.bookshelf.api.data.codecs.CodecHelper;
import net.darkhax.tipsmod.api.TipTypes;
import net.darkhax.tipsmod.api.TipsAPI;
import net.darkhax.tipsmod.api.resources.ITip;
import net.darkhax.tipsmod.impl.TipsModCommon;
import net.minecraft.network.chat.Component;

import java.util.Optional;

/**
 * A simple implementation of the tip.
 */
public class SimpleTip implements ITip {

    public static final CodecHelper<SimpleTip> CODEC = new CodecHelper<>(RecordCodecBuilder.create(instance -> instance.group(
            BookshelfCodecs.TEXT.get("title", SimpleTip::getTitle, TipsAPI.DEFAULT_TITLE),
            BookshelfCodecs.TEXT.get("tip", SimpleTip::getText),
            BookshelfCodecs.INT.getOptional("cycleTime", SimpleTip::getInternalCycleTime)
    ).apply(instance, SimpleTip::new)));

    public static final ByteBufHelper<SimpleTip> BUFFER = new ByteBufHelper<>(
            buffer -> {
                final Component title = BookshelfByteBufs.TEXT.read(buffer);
                final Component tip = BookshelfByteBufs.TEXT.read(buffer);
                final Optional<Integer> cycleTime = BookshelfByteBufs.INT.readOptional(buffer);
                return new SimpleTip(title, tip, cycleTime);
            },
            (buffer, toWrite) -> {
                BookshelfByteBufs.TEXT.write(buffer, toWrite.getTitle());
                BookshelfByteBufs.TEXT.write(buffer, toWrite.getText());
                BookshelfByteBufs.INT.writeOptional(buffer, toWrite.getInternalCycleTime());
            }
    );

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

    public SimpleTip(Component title, Component text, Optional<Integer> cycleTime) {

        this.title = title;
        this.text = text;
        this.cycleTime = cycleTime;
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

    @Override
    public TipTypes.TipType getType() {

        return TipTypes.SIMPLE_TIP_TYPE;
    }

    protected Optional<Integer> getInternalCycleTime() {

        return this.cycleTime;
    }
}