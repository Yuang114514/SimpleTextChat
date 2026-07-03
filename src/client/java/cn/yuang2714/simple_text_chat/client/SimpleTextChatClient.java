package cn.yuang2714.simple_text_chat.client;

import cn.yuang2714.simple_text_chat.SimpleTextChat;
import cn.yuang2714.simple_text_chat.client.core.ModelDictionary;
import cn.yuang2714.simple_text_chat.client.core.RecognizationManager;
import cn.yuang2714.simple_text_chat.client.minecrafts.HudTexts;
import cn.yuang2714.simple_text_chat.client.minecrafts.KeyMappings;
import cn.yuang2714.simple_text_chat.client.minecrafts.SetUpScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.Identifier;

public class SimpleTextChatClient implements ClientModInitializer {
    public static boolean isDisabled = false;
    
	@Override
	public void onInitializeClient() {
        System.setProperty("jna.encoding", "UTF-8");
        
        try {
            Config.init();
            ModelDictionary.init();
        } catch (Exception e) {
            SimpleTextChat.LOGGER.error("Failed to load files!", e);
            isDisabled = true;
        }
        
        KeyMapping featureToggleKey = KeyMappings.registerFeatureToggleKey();
        KeyMapping speechDoneKey = KeyMappings.registerSpeechDoneKey();
        KeyMapping speechCancelKey = KeyMappings.registerSpeechCancelKey();
        
        ClientTickEvents.END_CLIENT_TICK.register(_ -> {
            if (isDisabled) return;
            
            if (featureToggleKey.consumeClick())
                KeyMappings.featureToggleAction();
            if (speechDoneKey.consumeClick()) KeyMappings.speechDoneAction();
            if (speechCancelKey.consumeClick()) KeyMappings.speechCancelAction();
        });
        
        ScreenEvents.AFTER_INIT.register((minecraft, screen, _, _) -> {
            if (isDisabled) return;
            
            try {
                if (screen instanceof TitleScreen titleScreen && !Config.isSetup()) {
                    minecraft.setScreen(new SetUpScreen(titleScreen));
                }
            } catch (Exception _) {}
        });
        
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.fromNamespaceAndPath(SimpleTextChat.MOD_ID, "message_hud"), new HudTexts());
        
        ServerLevelEvents.UNLOAD.register(
                (_, _) -> {
                    if (!isDisabled && RecognizationManager.INSTANCE != null) {
                        RecognizationManager.INSTANCE.clean();
                    }
                }
        );
	}
}