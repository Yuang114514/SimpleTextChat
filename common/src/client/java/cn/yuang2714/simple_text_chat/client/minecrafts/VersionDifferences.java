package cn.yuang2714.simple_text_chat.client.minecrafts;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class VersionDifferences {
    public static MutableComponent translatable(String key, Object... args) {
        return Component.translatable(key, args);
    }
    
    public static ResourceLocation resourceLocationFromNamespaceAndPath(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }
}
