package cn.yuang2714.simple_text_chat.client.minecrafts;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyMappings {
    
    public static final String CATEGORY = "key.category.text_chat.keybind";
    
    public static KeyMapping featureToggleKey() {
        return new KeyMapping(
                "text.text_chat.key_mappings.feature_toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_COMMA,
                CATEGORY
        );
    }
    
    public static KeyMapping speechDoneKey() {
        return new KeyMapping(
                "text.text_chat.key_mappings.speech_done",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                CATEGORY
        );
    }
    
    public static KeyMapping speechCancelKey() {
        return new KeyMapping(
                "text.text_chat.key_mappings.speech_cancel",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                CATEGORY
        );
    }
}
