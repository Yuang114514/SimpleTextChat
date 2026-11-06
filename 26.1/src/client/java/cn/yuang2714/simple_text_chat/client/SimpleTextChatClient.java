package cn.yuang2714.simple_text_chat.client;

import cn.yuang2714.simple_text_chat.ConfigInitializationFailedException;
import cn.yuang2714.simple_text_chat.SimpleTextChat;
//import cn.yuang2714.simple_text_chat.api.SimpleTextChatRecognizationEvents;
import cn.yuang2714.simple_text_chat.client.config.Config;
import net.fabricmc.api.ClientModInitializer;

public class SimpleTextChatClient implements ClientModInitializer {
    public static boolean isDisabled = false;
    
	@Override
	public void onInitializeClient() {
        System.setProperty("jna.encoding", "UTF-8");
        
        try {
            Config.init();
        } catch (Exception e) {
            SimpleTextChat.LOGGER.error("Failed to load files!", e);
            //直接崩掉游戏
            throw new ConfigInitializationFailedException(e);
        }
        
//        KeyMapping featureToggleKey = KeyMappings.registerFeatureToggleKey();
//        KeyMapping speechDoneKey = KeyMappings.registerSpeechDoneKey();
//        KeyMapping speechCancelKey = KeyMappings.registerSpeechCancelKey();
//
//        ClientTickEvents.END_CLIENT_TICK.register(_ -> {
//            if (isDisabled) return;
//
//            if (featureToggleKey.consumeClick())
//                RecognizationManager.featureToggleAction();
//            if (speechDoneKey.consumeClick())
//                RecognizationManager.onFunctionKeyClick();
//            if (speechCancelKey.consumeClick())
//                RecognizationManager.onCancelKeyClick();
//        });
//
//        ScreenEvents.AFTER_INIT.register((minecraft, screen, _, _) -> {
//            if (isDisabled) return;
//
//            try {
//                if (screen instanceof TitleScreen titleScreen && !Config.isSetup()) {
//                    minecraft.setScreen(new ConfigScreen(titleScreen));
//                }
//            } catch (Exception _) {}
//        });
//
//        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.fromNamespaceAndPath(SimpleTextChat.MOD_ID, "message_hud"), new HudTexts());
//
//        ServerLevelEvents.UNLOAD.register(
//                (_, _) -> {
//                    if (!isDisabled && RecognizationManager.INSTANCE != null) {
//                        RecognizationManager.INSTANCE.clean();
//                    }
//                }
//        );
        
//        SimpleTextChatRecognizationEvents.PARTIAL_EVENT.register(evt -> SimpleTextChat.LOGGER.info(evt.toString()));
//        SimpleTextChatRecognizationEvents.RESULT_EVENT.register(evt -> SimpleTextChat.LOGGER.info(evt.toString()));
	}
}