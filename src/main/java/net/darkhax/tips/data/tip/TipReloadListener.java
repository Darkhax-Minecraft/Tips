package net.darkhax.tips.data.tip;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.darkhax.bookshelf.serialization.Serializers;
import net.darkhax.tips.Tips;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

/**
 * A reload listener that loads tip files from resource packs.
 */
public class TipReloadListener extends ReloadListener<Map<ResourceLocation, ITip>> {
    
    @Override
    protected void apply (Map<ResourceLocation, ITip> data, IResourceManager resources, IProfiler profiler) {
        
        Tips.LOG.info("Read {} tips.", data.size());
        Tips.API.updateTips(data);
    }
    
    @Override
    protected Map<ResourceLocation, ITip> prepare (IResourceManager resources, IProfiler profiler) {
        
        final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        final Map<ResourceLocation, ITip> map = Maps.newHashMap();
        
        for (final ResourceLocation candidate : resources.getAllResourceLocations("tips", n -> n.endsWith(".json"))) {
            
            final String path = candidate.getPath();
            final ResourceLocation entryId = new ResourceLocation(candidate.getNamespace(), path.substring("tips/".length(), path.length() - ".json".length()));
            
            if (!Tips.config.canLoadTip(entryId)) {
                
                Tips.LOG.debug("Skipping tip {} from {}. It was blocked by the config.", entryId, candidate);
                continue;
            }
            
            try {
                
                for (final IResource resource : resources.getAllResources(candidate)) {
                    
                    try (Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));) {
                        
                        final JsonObject json = gson.fromJson(reader, JsonObject.class);
                        
                        if (!CraftingHelper.processConditions(json, "conditions")) {
                            
                            Tips.LOG.debug("Skipping tip {} from {} as one or more of it's conditions were not met.", entryId, candidate);
                        }
                        
                        else {
                            
                            final ResourceLocation tipType = Serializers.RESOURCE_LOCATION.read(json, "type", SimpleTip.TYPE_ID);
                            final ITipSerializer<?> serializer = Tips.API.getTipSerializer(tipType);
                            
                            if (serializer != null) {
                                
                                final ITip tip = serializer.read(json);
                                
                                if (tip != null) {
                                    
                                    map.put(entryId, tip);
                                }
                                
                                else {
                                    
                                    Tips.LOG.debug("Skipping tip {} from {}. Serializer {} returned null.", entryId, candidate, tipType);
                                }
                            }
                            
                            else {
                                
                                Tips.LOG.error("Could not read tip {} from {}. Serializer {} does not exist!", entryId, candidate, tipType);
                            }
                        }
                    }
                }
            }
            
            catch (final Exception e) {
                
                Tips.LOG.error("Unable to read tip of {} from {}.", entryId, candidate, e);
            }
        }
        
        return map;
    }
}