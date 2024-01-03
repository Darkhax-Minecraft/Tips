package net.darkhax.tipsmod.impl;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final String MOD_ID = "tipsmod";
    public static final String MOD_NAME = "Tips";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static ResourceLocation id(String path) {

        return new ResourceLocation(MOD_ID, path);
    }
}
