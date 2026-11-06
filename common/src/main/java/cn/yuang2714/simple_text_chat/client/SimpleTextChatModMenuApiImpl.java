package cn.yuang2714.simple_text_chat.client;

import cn.yuang2714.simple_text_chat.client.minecrafts.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class SimpleTextChatModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<ConfigScreen> getModConfigScreenFactory() {
        return ConfigScreen::new;
    }
}
