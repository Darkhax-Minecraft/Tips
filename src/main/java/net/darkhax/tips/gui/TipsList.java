package net.darkhax.tips.gui;

import java.util.Locale;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.darkhax.tips.Tips;
import net.darkhax.tips.data.tip.ITip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TipsList extends AbstractOptionList<ListEntry> {
    
    public TipsList(TipsListScreen parent, Minecraft mc) {
        
        super(mc, parent.width, parent.height, 43, parent.height - 32, 50);
        this.refreshEntries(false, "");
    }
    
    public void refreshEntries (boolean showDisabled, String searchInput) {
        
        searchInput = searchInput.toLowerCase(Locale.ROOT);
        
        this.clearEntries();
        this.setScrollAmount(0d);
        for (final ITip tip : Tips.API.getTips().values()) {
            
            if ((showDisabled || Tips.CFG.canLoadTip(tip.getId())) && this.matchSearch(tip, searchInput)) {
                
                this.addEntry(new ListEntryTip(this.minecraft, tip));
            }
        }
    }
    
    private boolean matchSearch (ITip tip, String search) {
        
        if (search.isEmpty() || search == null) {
            
            return true;
        }
        
        if (tip.getId().toString().contains(search)) {
            
            return true;
        }
        
        if (TextFormatting.getTextWithoutFormattingCodes(tip.getTitle().getString()).toLowerCase(Locale.ROOT).contains(search)) {
            
            return true;
        }
        
        if (TextFormatting.getTextWithoutFormattingCodes(tip.getText().getString()).toLowerCase(Locale.ROOT).contains(search)) {
            
            return true;
        }
        
        return false;
    }
    
    @Override
    protected int getScrollbarPosition () {
        
        return super.getScrollbarPosition() + 15 + 20;
    }
    
    @Override
    public int getRowWidth () {
        
        return super.getRowWidth() + 32;
    }
    
    @Override
    public boolean mouseClicked (double mouseX, double mouseY, int button) {
        
        if (this.isMouseOver(mouseX, mouseY)) {
            
            for (int i = 0; i < this.getItemCount(); i++) {
                
                final ListEntry entry = this.getEntry(i);
                entry.isSelected = entry.isMouseOver(mouseX, mouseY);
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    protected void renderDecorations (MatrixStack matrix, int mouseX, int mouseY) {
        
        if (this.isMouseOver(mouseX, mouseY)) {
            
            for (int i = 0; i < this.getItemCount(); i++) {
                
                final ListEntry entry = this.getEntry(i);
                
                if (entry.isMouseOver(mouseX, mouseY)) {
                    
                    entry.renderMouseOver(matrix, mouseX, mouseY);
                }
            }
        }
    }
}