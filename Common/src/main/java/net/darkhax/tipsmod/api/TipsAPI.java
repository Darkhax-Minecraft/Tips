package net.darkhax.tipsmod.api;

import com.google.common.collect.ImmutableList;
import net.darkhax.bookshelf.Constants;
import net.darkhax.tipsmod.api.resources.ITip;
import net.darkhax.tipsmod.api.resources.ITipSerializer;
import net.darkhax.tipsmod.impl.TipsModCommon;
import net.darkhax.tipsmod.impl.resources.SimpleTip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TipsAPI {

    public static final ResourceLocation DEFAULT_SERIALIZER = new ResourceLocation("tips", "simple_tip");
    public static final Component DEFAULT_TITLE = Component.translatable("tipsmod.title.default").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE, ChatFormatting.YELLOW);
    public static final ITip EMPTY = new SimpleTip(new ResourceLocation(Constants.MOD_ID, "empty"), DEFAULT_TITLE, Component.literal("No tips loaded. Please review your config options!"), Optional.of(999999));
    private static Map<ResourceLocation, ITipSerializer<?>> SERIALIZERS = new HashMap<>();

    public static void registerTipSerializer(ResourceLocation id, ITipSerializer<?> serializer) {

        SERIALIZERS.put(id, serializer);
    }

    public static boolean canRenderOnScreen(Screen screen) {

        return screen instanceof GenericDirtMessageScreen || screen instanceof ConnectScreen || screen instanceof DisconnectedScreen || screen instanceof LevelLoadingScreen || screen instanceof ProgressScreen || screen instanceof PauseScreen || screen instanceof DeathScreen;
    }

    public static ITip getRandomTip() {

        final List<ITip> displayableTips = getLoadedTips().stream().filter(TipsAPI::canDisplayTip).toList();

        if (!displayableTips.isEmpty()) {

            final List<ITip> filteredTips = getLoadedTips().stream().filter(TipsAPI::canDisplayTip).toList();

            if (!filteredTips.isEmpty()) {

                return filteredTips.get(Constants.RANDOM.nextInt(filteredTips.size()));
            }
        }

        return EMPTY;
    }

    public static ITipSerializer<?> getTipSerializer(ResourceLocation id) {

        return SERIALIZERS.get(id);
    }

    public static List<ITip> getLoadedTips() {

        return TipsModCommon.TIP_MANAGER.getTips();
    }

    public static boolean canDisplayTip(ITip tip) {

        final ResourceLocation id = tip.getId();

        if (TipsModCommon.CONFIG.ignoredNamespaces.contains(id.getNamespace())) {

            return false;
        }

        if (TipsModCommon.CONFIG.ignoredTips.contains(id.toString())) {

            return false;
        }

        return true;
    }
}
