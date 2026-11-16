package cn.yuang2714.simple_text_chat.client;

import cn.yuang2714.simple_text_chat.client.mcver.IMinecraftVersionHelper;
import cn.yuang2714.simple_text_chat.client.minecrafts.HudTexts;
import cn.yuang2714.simple_text_chat.client.minecrafts.KeyMappings;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import org.lwjgl.glfw.GLFW;

import java.util.function.Function;

public class Minecraft1_21VersionHelper implements IMinecraftVersionHelper {
    @Override
    public void setScreen(Screen s) {
        Minecraft.getInstance().setScreen(s);
    }
    
    @Override
    public KeyMapping getFeatureToggleKey() {
        return new KeyMapping(
                "text.text_chat.key_mappings.feature_toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_COMMA,
                KeyMappings.CATEGORY
        );
    }
    
    @Override
    public KeyMapping getSpeechDoneKey() {
        return new KeyMapping(
                "text.text_chat.key_mappings.speech_done",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                KeyMappings.CATEGORY
        );
    }
    
    @Override
    public KeyMapping getSpeechCancelKey() {
        return new KeyMapping(
                "text.text_chat.key_mappings.speech_cancel",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                KeyMappings.CATEGORY
        );
    }
    
    @Override
    public void openUri(String uri) {
        Util.getPlatform().openUri(uri);
    }
    
    @Override
    public HoverEvent getShowTextEvent(Component text) {
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, text);
    }
    
    @Override
    public <T> CycleButton.Builder<T> cycleButtonBuilder(Function<T, Component> onValueChange, T initialValue) {
        return CycleButton.builder(onValueChange).withInitialValue(initialValue);
    }
    
    @Override
    public void registerHud() {
        HudRenderCallback.EVENT.register((graphics, delta) -> HudTexts.onHudRender(graphics));
    }
    
    @Override
    public MultiLineEditBox createMultiLineEditBox(Font font, int x, int y, int width, int height) {
        return new MultiLineEditBox(
                font,
                x,
                y,
                width,
                height,
                Component.empty(),
                Component.empty()
        );
    }
}
