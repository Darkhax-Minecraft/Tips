package net.darkhax.tips.gui;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ButtonCheckbox extends CheckboxButton {
    
    private static final StringTextComponent EMPTY = new StringTextComponent("");
    
    @Nullable
    private final Consumer<Boolean> pressAction;
    
    public ButtonCheckbox(int x, int y, int width, int height, boolean defaultValue) {
        
        this(x, y, width, height, EMPTY, defaultValue, false, null);
    }
    
    public ButtonCheckbox(int x, int y, int width, int height, ITextComponent title, boolean defaultValue, Boolean showTitle) {
        
        this(x, y, width, height, title, defaultValue, showTitle, null);
    }
    
    public ButtonCheckbox(int x, int y, int width, int height, ITextComponent title, boolean defaultValue, @Nullable Consumer<Boolean> pressAction) {
        
        this(x, y, width, height, title, defaultValue, true, pressAction);
    }
    
    public ButtonCheckbox(int x, int y, int width, int height, ITextComponent title, boolean defaultValue, Boolean showTitle, @Nullable Consumer<Boolean> pressAction) {
        
        super(x, y, width, height, title, defaultValue, showTitle);
        this.pressAction = pressAction;
    }
    
    @Override
    public void onPress () {
        
        super.onPress();
        
        if (this.pressAction != null) {
            
            this.pressAction.accept(this.isChecked());
        }
    }
}