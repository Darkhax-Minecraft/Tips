package net.darkhax.tipsmod.impl.gui;

import java.util.Locale;

import com.mojang.blaze3d.vertex.PoseStack;
import net.darkhax.tipsmod.api.TipsAPI;
import net.darkhax.tipsmod.api.resources.ITip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;

public class TipsList extends AbstractSelectionList<ListEntry> {
    
    public TipsList(TipsListScreen parent, Minecraft mc) {
        
        super(mc, parent.width, parent.height, 43, parent.height - 32, 50);
        this.refreshEntries(false, "");
    }
    
    public void refreshEntries (boolean showDisabled, String searchInput) {
        
        searchInput = searchInput.toLowerCase(Locale.ROOT);
        
        this.clearEntries();
        this.setScrollAmount(0d);
        for (final ITip tip : TipsAPI.getLoadedTips()) {
            
            if ((showDisabled || TipsAPI.canDisplayTip(tip)) && this.matchSearch(tip, searchInput)) {
                
                this.addEntry(new ListEntryTip(this.minecraft, tip));
            }
        }
    }
    
    private boolean matchSearch (ITip tip, String search) {
        
        if (search.isEmpty() || search == null) {
            
            return true;
        }

        // Search the ID of the tip
        if (tip.getId().toString().contains(search)) {
            
            return true;
        }

        // Search the title text of the tip.
        if (ChatFormatting.stripFormatting(tip.getTitle().getString()).toLowerCase(Locale.ROOT).contains(search)) {
            
            return true;
        }

        // Search the body text of the tip.
        if (ChatFormatting.stripFormatting(tip.getText().getString()).toLowerCase(Locale.ROOT).contains(search)) {
            
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
    protected void renderDecorations (PoseStack matrix, int mouseX, int mouseY) {
        
        if (this.isMouseOver(mouseX, mouseY)) {
            
            for (int i = 0; i < this.getItemCount(); i++) {
                
                final ListEntry entry = this.getEntry(i);
                
                if (entry.isMouseOver(mouseX, mouseY)) {
                    
                    entry.renderMouseOver(matrix, mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput narrator) {

        this.getHovered().updateNarrator(narrator);
    }
}