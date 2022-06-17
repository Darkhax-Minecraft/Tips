package net.darkhax.tipsmod.impl.gui;

import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class FunctionalCheckbox extends Checkbox {
    
    private static final Component EMPTY = Component.empty();

    private final Consumer<Boolean> pressAction;
    
    public FunctionalCheckbox(int x, int y, int width, int height, boolean defaultValue) {
        
        this(x, y, width, height, EMPTY, defaultValue, false, null);
    }
    
    public FunctionalCheckbox(int x, int y, int width, int height, Component title, boolean defaultValue, Boolean showTitle) {
        
        this(x, y, width, height, title, defaultValue, showTitle, null);
    }
    
    public FunctionalCheckbox(int x, int y, int width, int height, Component title, boolean defaultValue, Consumer<Boolean> pressAction) {
        
        this(x, y, width, height, title, defaultValue, true, pressAction);
    }
    
    public FunctionalCheckbox(int x, int y, int width, int height, Component title, boolean defaultValue, Boolean showTitle, Consumer<Boolean> pressAction) {
        
        super(x, y, width, height, title, defaultValue, showTitle);
        this.pressAction = pressAction;
    }
    
    @Override
    public void onPress () {

        super.onPress();
        
        if (this.pressAction != null) {
            
            this.pressAction.accept(this.selected());
        }
    }
}