package net.darkhax.tips.client.shader;

import net.darkhax.tips.TipsAPI;
import net.minecraft.client.shader.Framebuffer;

/**
 * This class is used as a hack for injecting rendering code into the world/server loading
 * screen menu. This is done by replacing the frame buffer in LoadingScreenRenderer, and then
 * calling some additional render code before the buffer has been unbound.
 * 
 * This approach was originally used in DeflatedPickle's mod JustTheTip. While this mod has not
 * used any code from this mod, this approach was directly inspired by this project. You can
 * find this mod here: https://github.com/DeflatedPickle/JustTheTips
 * 
 * This delegate modifies {@link #createBindFramebuffer(int, int)} and
 * {@link #unbindFramebuffer()}.
 */
public class WrappedFrameBuffer extends Framebuffer {
    
    private final Framebuffer delegate;
    
    public WrappedFrameBuffer(Framebuffer delegate) {
        
        super(delegate.framebufferWidth, delegate.framebufferHeight, delegate.useDepth);
        this.delegate = delegate;
    }
    
    @Override
    public void createBindFramebuffer (int width, int height) {
        
        // The delegate frame buffer has presumably already been bind.
        // Trying to bind it again will cause exceptions to be thrown.
    }
    
    @Override
    public int hashCode () {
        
        return this.delegate.hashCode();
    }
    
    @Override
    public void deleteFramebuffer () {
        
        this.delegate.deleteFramebuffer();
    }
    
    @Override
    public void createFramebuffer (int width, int height) {
        
        this.delegate.createFramebuffer(width, height);
    }
    
    @Override
    public boolean equals (Object obj) {
        
        return this.delegate.equals(obj);
    }
    
    @Override
    public void setFramebufferFilter (int framebufferFilterIn) {
        
        this.delegate.setFramebufferFilter(framebufferFilterIn);
    }
    
    @Override
    public void checkFramebufferComplete () {
        
        this.delegate.checkFramebufferComplete();
    }
    
    @Override
    public void bindFramebufferTexture () {
        
        this.delegate.bindFramebufferTexture();
    }
    
    @Override
    public void unbindFramebufferTexture () {
        
        this.delegate.unbindFramebufferTexture();
    }
    
    @Override
    public void bindFramebuffer (boolean shouldBind) {
        
        this.delegate.bindFramebuffer(shouldBind);
    }
    
    @Override
    public void unbindFramebuffer () {
        
        TipsAPI.renderTip();
        this.delegate.unbindFramebuffer();
    }
    
    @Override
    public void setFramebufferColor (float red, float green, float blue, float alpha) {
        
        this.delegate.setFramebufferColor(red, green, blue, alpha);
    }
    
    @Override
    public void framebufferRender (int width, int height) {
        
        this.delegate.framebufferRender(width, height);
    }
    
    @Override
    public void framebufferRenderExt (int width, int height, boolean p_178038_3_) {
        
        this.delegate.framebufferRenderExt(width, height, p_178038_3_);
    }
    
    @Override
    public String toString () {
        
        return this.delegate.toString();
    }
    
    @Override
    public void framebufferClear () {
        
        this.delegate.framebufferClear();
    }
    
    @Override
    public boolean enableStencil () {
        
        return this.delegate.enableStencil();
    }
    
    @Override
    public boolean isStencilEnabled () {
        
        return this.delegate.isStencilEnabled();
    }
}