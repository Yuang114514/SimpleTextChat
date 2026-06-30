package cn.yuang2714.simple_text_chat.client.minecrafts;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SetUpScreen extends Screen {
    protected SetUpScreen() {
        super(Component.translatable("gui.text_chat.setup.title"));
    }
}
