package cn.yuang2714.simple_text_chat.client.mcver;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.Font;import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.MultiLineEditBox;import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;

import java.util.function.Function;

public interface IMinecraftVersionHelper {
    void setScreen(Screen screen);
    
    KeyMapping getFeatureToggleKey();
    KeyMapping getSpeechDoneKey();
    KeyMapping getSpeechCancelKey();
    
    void openUri(String uri);
    
    HoverEvent getShowTextEvent(Component text);
    
    <T> CycleButton.Builder<T> cycleButtonBuilder(Function<T, Component> onValueChange, T initialValue);
    
    void registerHud();
    
    MultiLineEditBox createMultiLineEditBox(Font font, int x, int y, int width, int height);
}
