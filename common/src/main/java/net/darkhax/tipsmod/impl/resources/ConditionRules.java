package net.darkhax.tipsmod.impl.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.darkhax.tipsmod.api.TipsAPI;
import net.darkhax.tipsmod.impl.Constants;
import net.darkhax.tipsmod.impl.client.VanillaScreenIds;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ConditionRules<T> implements Predicate<T> {

    @Nullable
    private final RuleGroup<T> anyOf;

    @Nullable
    private final RuleGroup<T> allOf;

    @Nullable
    private final RuleGroup<T> noneOf;

    private ConditionRules(@Nullable RuleGroup<T> any, @Nullable RuleGroup<T> all, @Nullable RuleGroup<T> none) {
        this.anyOf = any;
        this.allOf = all;
        this.noneOf = none;
    }

    public boolean isEmpty() {
        return anyOf == null && allOf == null && noneOf == null;
    }

    @Override
    public boolean test(T t) {
        return (anyOf == null || anyOf.test(t)) && (allOf == null || allOf.test(t)) && (noneOf == null || noneOf.test(t));
    }

    @Nullable
    public JsonElement writeJson() {
        if (anyOf != null && allOf == null && noneOf == null) {
            return Serializers.STRING.toJSONSet(anyOf.entries);
        }
        else {
            final JsonObject json = new JsonObject();
            if (anyOf != null) {
                json.add("any_of", Serializers.STRING.toJSONSet(anyOf.entries));
            }
            if (allOf != null) {
                json.add("all_of", Serializers.STRING.toJSONSet(allOf.entries));
            }
            if (noneOf != null) {
                json.add("none_of", Serializers.STRING.toJSONSet(noneOf.entries));
            }
            if (!json.keySet().isEmpty()) {
                return json;
            }
        }
        return null;
    }

    public static class RuleGroup<T> implements Predicate<T> {

        private final Predicate<T> predicate;
        private final Set<String> entries;

        private RuleGroup(Predicate<T> predicate, Set<String> entries) {
            this.predicate = predicate;
            this.entries = entries;
        }

        public boolean isEmpty() {

            return this.entries.isEmpty();
        }

        @Override
        public boolean test(T t) {
            return this.predicate.test(t);
        }
    }

    public static Predicate<Screen> screenRuleBuilder(String rule) {

        if (rule.equalsIgnoreCase("tipsmod:built-in")) {
            return TipsAPI::canRenderOnScreen;
        }

        // Match by VanillaScreenIds class
        else if (rule.contains(":")) {
            final ResourceLocation targetScreen = ResourceLocation.tryParse(rule);
            if (targetScreen != null && "minecraft".equalsIgnoreCase(targetScreen.getNamespace())) {
                return screen -> VanillaScreenIds.is(targetScreen, screen.getClass());
            }
            Constants.LOG.error("Screen condition with ID {} is not valid. Only the vanilla screens have IDs.", rule);
        }

        // Match by canonical class name
        else if (rule.contains(".")) {
            return screen -> rule.equalsIgnoreCase(screen.getClass().getCanonicalName());
        }

        // Match by simplified class name.
        else {
            return screen -> rule.equals(screen.getClass().getSimpleName());
        }

        return screen -> false;
    }

    public static <T> Predicate<Holder<T>> registryRuleBuilder(Function<ResourceLocation, TagKey<T>> tagBuilder, String rule) {

        // Match by ID
        if (ResourceLocation.isValidResourceLocation(rule)) {
            final ResourceLocation targetEntryID = ResourceLocation.tryParse(rule);
            return targetEntryID == null ? entry -> false : entry -> entry.is(targetEntryID);
        }

        // Match by Namespace
        else if (isNamespace(rule)) {
            return entry -> entry.unwrapKey().map(id -> rule.equalsIgnoreCase(id.location().getNamespace())).orElse(false);
        }

        // Match by tag
        else if (rule.startsWith("#") && ResourceLocation.isValidResourceLocation(rule.substring(1))) {
            final TagKey<T> tag = tagBuilder.apply(ResourceLocation.tryParse(rule.substring(1)));
            return entry -> entry.is(tag);
        }

        // Try with Regex
        else if (rule.startsWith("~")) {

            try {
                final Pattern pattern = Pattern.compile(rule);
                return entry -> entry.unwrapKey().map(key -> pattern.matcher(key.toString()).matches()).orElse(false);
            }
            catch (PatternSyntaxException e) {
                Constants.LOG.error("An invalid Regex pattern was used! Rule '{}' is invalid!", rule, e);
                return entry -> false;
            }
        }

        Constants.LOG.error("An invalid pattern was used. Pattern must be a valid resource location, namespace, tag, or regex pattern. '{}'", rule);
        return entry -> false;
    }

    public static Predicate<Set<ResourceLocation>> resourceLocationsRuleBuilder(String rule) {
        final Predicate<ResourceLocation> rlRule = resourceLocationRuleBuilder(rule);
        return list -> list.stream().anyMatch(rlRule);
    }

    public static Predicate<ResourceLocation> resourceLocationRuleBuilder(String rule) {

        // Match by ID
        if (ResourceLocation.isValidResourceLocation(rule)) {
            final ResourceLocation targetEntryID = ResourceLocation.tryParse(rule);
            return targetEntryID == null ? entry -> false : entry -> entry.equals(targetEntryID);
        }

        // Match by Namespace
        else if (isNamespace(rule)) {
            return entry -> rule.equalsIgnoreCase(entry.getNamespace());
        }

        // Try with Regex
        else if (rule.startsWith("~")) {

            try {
                final Pattern pattern = Pattern.compile(rule);
                return entry -> pattern.matcher(entry.toString()).matches();
            }
            catch (PatternSyntaxException e) {
                Constants.LOG.error("An invalid Regex pattern was used! Rule '{}' is invalid!", rule, e);
                return entry -> false;
            }
        }

        Constants.LOG.error("An invalid pattern was used. Pattern must be a valid resource location, namespace, or regex pattern. '{}'", rule);
        return entry -> false;
    }

    private static boolean isNamespace(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (!allowedInNamespace(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean allowedInNamespace(char input) {
        return input == '_' || input == '-' || input >= 'a' && input <= 'z' || input >= '0' && input <= '9' || input == '.';
    }

    @Nullable
    public static <T> ConditionRules<T> fromElement(Function<String, Predicate<T>> ruleBuilder, @Nullable JsonElement element) {

        // Missing / No Rules = Allow
        if (element == null) {
            return new ConditionRules<>(null, null, null);
        }

        // Just an array/string = Any match
        if (element instanceof JsonArray || (element instanceof JsonPrimitive primitive && primitive.isString())) {
            final Set<String> rules = Serializers.STRING.fromJSONSet(element);
            return new ConditionRules<>(new RuleGroup<>(v -> buildRules(ruleBuilder, rules).stream().anyMatch(rule -> rule.test(v)), rules), null, null);
        }

        if (element instanceof JsonObject obj) {

            RuleGroup<T> anyOf = null;
            RuleGroup<T> allOf = null;
            RuleGroup<T> noneOf = null;

            // All rules must match
            if (obj.has("all_of")) {
                final Set<String> rules = Serializers.STRING.fromJSONSet(obj, "all_of");
                allOf = new RuleGroup<>(v -> buildRules(ruleBuilder, rules).stream().allMatch(rule -> rule.test(v)), rules);
            }

            // No rules must match
            if (obj.has("none_of")) {
                final Set<String> rules = Serializers.STRING.fromJSONSet(obj, "none_of");
                noneOf = new RuleGroup<>(v -> buildRules(ruleBuilder, rules).stream().noneMatch(rule -> rule.test(v)), rules);
            }

            // Any of the rules must match.
            if (obj.has("any_of")) {
                final Set<String> rules = Serializers.STRING.fromJSONSet(obj, "any_of");
                anyOf = new RuleGroup<>(v -> buildRules(ruleBuilder, rules).stream().anyMatch(rule -> rule.test(v)), rules);
            }

            if (anyOf == null && allOf == null && noneOf == null) {
                Constants.LOG.warn("You have configured a tip with conditions, but none of the conditions were valid! You need an 'any_of', 'all_of', or 'none_of' property. Condition={}", obj.getAsString());
                return null;
            }

            return new ConditionRules<>(anyOf, allOf, noneOf);
        }

        throw new JsonParseException("Condition rules must be a string, string array, or object with an 'all', 'any', or 'none' property.");
    }

    private static <T> List<Predicate<T>> buildRules(Function<String, Predicate<T>> ruleBuilder, Set<String> rules) {
        return rules.stream().map(ruleBuilder).toList();
    }
}
