package cn.yuang2714.simple_text_chat.client.mcver;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;

public interface IMinecraftVersionHelper {
    void setScreen(Screen screen);
    
    KeyMapping getFeatureToggleKey();
    KeyMapping getSpeechDoneKey();
    KeyMapping getSpeechCancelKey();
    
    void openUri(String uri);
}
