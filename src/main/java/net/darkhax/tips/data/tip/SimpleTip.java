package net.darkhax.tips.data.tip;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.darkhax.bookshelf.serialization.Serializers;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * A simple implementation of the tip.
 */
public class SimpleTip implements ITip {
    
    /**
     * The serializer ID for this type of tip.
     */
    public static final ResourceLocation TYPE_ID = new ResourceLocation("tips", "simple_tip");
    
    /**
     * The serializer for this type of tip.
     */
    public static final ITipSerializer<?> SERIALIZER = new Serializer();
    
    /**
     * The title text to display.
     */
    private final ITextComponent title;
    
    /**
     * The body of the tip.
     */
    private final ITextComponent text;
    
    public SimpleTip(ITextComponent title, ITextComponent text) {
        
        this.title = title;
        this.text = text;
    }
    
    @Override
    public ITextComponent getTitle () {
        
        return this.title;
    }
    
    @Override
    public ITextComponent getText () {
        
        return this.text;
    }
    
    /**
     * The serializer for SimpleTip.
     */
    static final class Serializer implements ITipSerializer<SimpleTip> {
        
        /**
         * The default tip tile, used when no title is defined.
         */
        private static final ITextComponent DEFAULT_TITLE = new TranslationTextComponent("tips.title.tip").mergeStyle(TextFormatting.BOLD, TextFormatting.UNDERLINE, TextFormatting.YELLOW);
        
        @Override
        public SimpleTip read (JsonObject json) {
            
            final ITextComponent title = Serializers.TEXT.read(json, "title", DEFAULT_TITLE);
            final ITextComponent text = Serializers.TEXT.read(json, "tip");
            return new SimpleTip(title, text);
        }
        
        @Override
        public JsonElement write (SimpleTip toWrite) {
            
            final JsonObject json = new JsonObject();
            json.add("type", Serializers.RESOURCE_LOCATION.write(SimpleTip.TYPE_ID));
            json.add("title", Serializers.TEXT.write(toWrite.title));
            json.add("tip", Serializers.TEXT.write(toWrite.text));
            return json;
        }
    }
}