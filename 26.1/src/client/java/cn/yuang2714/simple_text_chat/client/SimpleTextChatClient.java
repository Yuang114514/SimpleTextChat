package cn.yuang2714.simple_text_chat.client;

//import cn.yuang2714.simple_text_chat.api.SimpleTextChatRecognizationEvents;
import cn.yuang2714.simple_text_chat.client.mcver.MinecraftVersionHelper;
import net.fabricmc.api.ClientModInitializer;

public class SimpleTextChatClient implements ClientModInitializer {
    
	@Override
	public void onInitializeClient() {
        MinecraftVersionHelper.setHelper(new Minecraft26_1VersionHelper());
        SimpleTextChatCommon.onInitializeClient();
	}
}