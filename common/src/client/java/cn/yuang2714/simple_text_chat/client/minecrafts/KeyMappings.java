package cn.yuang2714.simple_text_chat.client.minecrafts;

import cn.yuang2714.simple_text_chat.client.mcver.MinecraftVersionHelper;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class KeyMappings {
    
    public static final String CATEGORY = "key.category.text_chat.keybind";
    
    public static KeyMapping registerFeatureToggleKey() {
        return KeyBindingHelper.registerKeyBinding(
                MinecraftVersionHelper.HELPER.getFeatureToggleKey()
//                new KeyMapping(
//                        "text.text_chat.key_mappings.feature_toggle",
//                        InputConstants.Type.KEYSYM,
//                        GLFW.GLFW_KEY_COMMA,
//                        CATEGORY
//                )
        );
    }
    
    public static KeyMapping registerSpeechDoneKey() {
        return KeyBindingHelper.registerKeyBinding(
                MinecraftVersionHelper.HELPER.getSpeechDoneKey()
//                new KeyMapping(
//                        "text.text_chat.key_mappings.speech_done",
//                        InputConstants.Type.KEYSYM,
//                        GLFW.GLFW_KEY_X,
//                        CATEGORY
//                )
        );
    }
    
    public static KeyMapping registerSpeechCancelKey() {
        return KeyBindingHelper.registerKeyBinding(
                MinecraftVersionHelper.HELPER.getSpeechCancelKey()
//                new KeyMapping(
//                        "text.text_chat.key_mappings.speech_cancel",
//                        InputConstants.Type.KEYSYM,
//                        GLFW.GLFW_KEY_C,
//                        CATEGORY
//                )
        );
    }
}
