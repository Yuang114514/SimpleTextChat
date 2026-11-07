package cn.yuang2714.simple_text_chat.client;

import cn.yuang2714.simple_text_chat.client.mcver.IMinecraftVersionHelper;
import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Util;

import java.net.URI;
import java.net.URISyntaxException;

public class Minecraft26_3VersionHelper implements IMinecraftVersionHelper {
    public void setScreen(Screen screen) {
        Minecraft.getInstance().gui.setScreen(screen);
    }
    
    @Override
    public KeyMapping getFeatureToggleKey() {
        return new KeyMapping(
                "text.text_chat.key_mappings.feature_toggle",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_COMMA,
                cn.yuang2714.simple_text_chat.client.minecrafts.KeyMappings.CATEGORY
        );
    }
    
    @Override
    public KeyMapping getSpeechDoneKey() {
        return new KeyMapping(
                "text.text_chat.key_mappings.speech_done",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_X,
                cn.yuang2714.simple_text_chat.client.minecrafts.KeyMappings.CATEGORY
        );
    }
    
    @Override
    public KeyMapping getSpeechCancelKey() {
        return new KeyMapping(
                "text.text_chat.key_mappings.speech_cancel",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_C,
                cn.yuang2714.simple_text_chat.client.minecrafts.KeyMappings.CATEGORY
        );
    }
    
    @Override
    public void openUri(String uri) {
        try {
            Blaze3D.openUri(new URI(uri));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
