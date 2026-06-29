package cn.yuang2714.simple_text_chat.client;

import cn.yuang2714.simple_text_chat.client.minecrafts.KeyMappings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;

public class SimpleTextChatClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        KeyMapping featureToggleKey = KeyMappings.registerFeatureToggleKey();
        KeyMapping speechDoneKey = KeyMappings.registerSpeechDoneKey();
        KeyMapping speechCancelKey = KeyMappings.registerSpeechCancelKey();
        
        ClientTickEvents.END_CLIENT_TICK.register(_ -> {
            if (featureToggleKey.consumeClick()) KeyMappings.featureToggleAction();
            if (speechDoneKey.consumeClick()) KeyMappings.speechDoneAction();
            if (speechCancelKey.consumeClick()) KeyMappings.speechCancelAction();
        });
	}
}