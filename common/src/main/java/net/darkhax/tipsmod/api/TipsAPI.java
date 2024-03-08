package net.darkhax.tipsmod.api;

import net.darkhax.bookshelf.Constants;
import net.darkhax.tipsmod.api.resources.ITip;
import net.darkhax.tipsmod.api.resources.ITipSerializer;
import net.darkhax.tipsmod.impl.TipsModCommon;
import net.darkhax.tipsmod.impl.resources.SimpleTip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class TipsAPI {

    public static final ResourceLocation DEFAULT_SERIALIZER = new ResourceLocation("tips", "simple_tip");
    public static final Component DEFAULT_TITLE = Component.translatable("tipsmod.title.default").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE, ChatFormatting.YELLOW);
    private static final Map<ResourceLocation, ITipSerializer<?>> SERIALIZERS = new HashMap<>();
    private static final Set<Class<? extends Screen>> SCREENS = new HashSet<>();
    private static int currentTipIndex = 0;

    @Deprecated
    public static final ITip EMPTY = new SimpleTip(new ResourceLocation(Constants.MOD_ID, "empty"), DEFAULT_TITLE, Component.literal("This is deprecated and should no longer be used."), Optional.of(0));

    public static void registerTipSerializer(ResourceLocation id, ITipSerializer<?> serializer) {

        SERIALIZERS.put(id, serializer);
    }

    public static void registerTipScreen(Class<? extends Screen> screenClass) {

        SCREENS.add(screenClass);
    }

    public static boolean canRenderOnScreen(Screen screen) {

        return canRenderOnScreen(screen.getClass());
    }

    public static boolean canRenderOnScreen(Class<?> clazz) {

        return SCREENS.contains(clazz) && !TipsModCommon.CONFIG.ignoredScreens.contains(clazz.getCanonicalName());
    }

    public static Collection<Class<?>> getTipsScreens() {

        return Collections.unmodifiableSet(SCREENS);
    }

    @Nullable
    @Deprecated
    public static ITip getRandomTip() {
        return getRandomTip(Minecraft.getInstance().screen);
    }

    @Nullable
    public static ITip getRandomTip(Screen screen) {

        final List<ITip> filteredTips = getLoadedTips().stream().filter(tip -> TipsAPI.canDisplayTip(tip) && tip.canDisplayOnScreen(screen)).toList();

        if (currentTipIndex + 1 > filteredTips.size()) {

            currentTipIndex = 0;
        }

        if (!filteredTips.isEmpty()) {

            return filteredTips.get(currentTipIndex++);
        }

        return null;
    }

    public static ITipSerializer<?> getTipSerializer(ResourceLocation id) {

        return SERIALIZERS.get(id);
    }

    public static List<ITip> getLoadedTips() {

        return TipsModCommon.TIP_MANAGER.getTips();
    }

    public static boolean canDisplayTip(ITip tip) {

        if (tip == null) {
            return false;
        }

        if (tip.getId() == null) {
            Constants.LOG.error("Found invalid tip without an ID. Object: {}, Class: {}", tip, tip.getClass());
            return false;
        }

        else if (tip.getTitle() == null) {
            Constants.LOG.error("Found invalid tip. Title is null. Object: {}, Class: {}, ID: {}", tip, tip.getClass(), tip.getId());
            return false;
        }

        else if (tip.getText() == null) {
            Constants.LOG.error("Found invalid tip. Text is null. Object: {}, Class: {}, ID: {}", tip, tip.getClass(), tip.getId());
            return false;
        }

        final ResourceLocation id = tip.getId();

        if (TipsModCommon.CONFIG.ignoredNamespaces.contains(id.getNamespace())) {
            return false;
        }

        if (TipsModCommon.CONFIG.ignoredTips.contains(id.toString())) {
            return false;
        }

        if (TipsModCommon.CONFIG.hideUnlocalizedTips && tip.getText().getContents() instanceof TranslatableContents translatable && !I18n.exists(translatable.getKey())) {
            return false;
        }

        return true;
    }
}