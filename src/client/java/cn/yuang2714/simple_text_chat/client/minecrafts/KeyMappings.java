package cn.yuang2714.simple_text_chat.client.minecrafts;

import cn.yuang2714.simple_text_chat.client.core.RecognizationManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyMappings {
    public static boolean isEnabled = false;
    
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            VersionDifferences.resourceLocationFromNamespaceAndPath("text_chat", "keybind")
    );
    
    public static KeyMapping registerFeatureToggleKey() {
        return KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "text.text_chat.key_mappings.feature_toggle",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_COMMA,
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
        
        if (isEnabled) {
            RecognizationManager.setup();
        } else {
            RecognizationManager.INSTANCE.clean();
            HudTexts.text = VersionDifferences.translatable("hud.text_chat.status.disabled").withStyle(ChatFormatting.YELLOW);
        }
    }
    
    public static void speechDoneAction() {
        RecognizationManager.functionKey.submit();
    }
    
    public static void speechCancelAction() {
        RecognizationManager.cancelKey.submit();
    }
}
