package cn.yuang2714.simple_text_chat.client;

import cn.yuang2714.simple_text_chat.client.mcver.IMinecraftVersionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class Minecraft26_1VersionHelper implements IMinecraftVersionHelper {
    @Override
    public void setScreen(Screen screen) {
        Minecraft.getInstance().setScreen(screen);
    }
}
