package cn.yuang2714.simple_text_chat.client.minecrafts;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

public class VersionDifferences {
    public static MutableComponent translatableComponent(String key, Object... args) {
        return Component.translatable(key, args);
    }
    
    public static MutableComponent literalComponent(String s) {
        return Component.literal(s);
    }
    
    public static Identifier resourceLocationFromNamespaceAndPath(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }
}
