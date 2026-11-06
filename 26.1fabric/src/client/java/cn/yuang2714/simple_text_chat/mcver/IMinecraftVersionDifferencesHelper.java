package cn.yuang2714.simple_text_chat.mcver;

import cn.yuang2714.simple_text_chat.client.minecrafts.ConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.function.Supplier;

public interface IMinecraftVersionDifferencesHelper {
    Supplier<? extends ConfigScreen> configScreenFactory();
    Component translatableComponent(String key, Object... args);
    void setScreen(Screen screen);
    
    Path getConfigDir();
}
