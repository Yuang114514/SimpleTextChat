package cn.yuang2714.simple_text_chat.client.minecrafts;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyMappings {
    public static boolean isEnabled = false;
    
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("text_chat", "keybind")
    );
    
    public static KeyMapping registerFeatureToggleKey() {
        return KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "text.text_chat.key_mappings.feature_toggle",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_P,
                        CATEGORY
                )
        );
    }
    
    public static KeyMapping registerSpeechDoneKey() {
        return KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "text.text_chat.key_mappings.speech_done",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_X,
                        CATEGORY
                )
        );
    }
    
    public static KeyMapping registerSpeechCancelKey() {
        return KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "text.text_chat.key_mappings.speech_cancel",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_C,
                        CATEGORY
                )
        );
    }
    
    public static void featureToggleAction() {
        isEnabled = !isEnabled;
    }
    
    public static void speechDoneAction() {}
    
    public static void speechCancelAction() {}
}
