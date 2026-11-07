package cn.yuang2714.simple_text_chat.client.minecrafts;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class VersionDifferences {
    public static MutableComponent translatable(String key, Object... args) {
        return Component.translatable(key, args);
    }
}
