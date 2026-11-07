package cn.yuang2714.simple_text_chat.client;

import cn.yuang2714.simple_text_chat.client.mcver.IMinecraftVersionHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;
import cn.yuang2714.simple_text_chat.client.minecrafts.KeyMappings;

public class Minecraft26_1VersionHelper implements IMinecraftVersionHelper {
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
}
