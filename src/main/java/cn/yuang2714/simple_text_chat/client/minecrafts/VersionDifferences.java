package cn.yuang2714.simple_text_chat.client.minecrafts;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;

public class VersionDifferences {
    public static MutableComponent translatable(String key, Object... args) {
        return Component.translatable(key, args);
    }
    
    public static void openUri(String uri) {
        Util.getPlatform().openUri(uri);
    }
    
    public static void setScreen(@Nullable Minecraft minecraft, Screen screen) {
        if (minecraft != null) minecraft.setScreen(screen);
        else setScreen(screen);
    }
    
    public static void setScreen(Screen screen) {
        Minecraft.getInstance().setScreen(screen);
    }
}
