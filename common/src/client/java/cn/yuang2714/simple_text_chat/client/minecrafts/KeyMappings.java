package cn.yuang2714.simple_text_chat.client.minecrafts;

import cn.yuang2714.simple_text_chat.client.mcver.MinecraftVersionHelper;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

public class KeyMappings {
    
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            VersionDifferences.resourceLocationFromNamespaceAndPath("text_chat", "keybind")
    );
    
    public static KeyMapping registerFeatureToggleKey() {
        return KeyMappingHelper.registerKeyMapping(
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
        return KeyMappingHelper.registerKeyMapping(
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
        return KeyMappingHelper.registerKeyMapping(
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
