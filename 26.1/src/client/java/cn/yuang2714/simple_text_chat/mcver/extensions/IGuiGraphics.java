package cn.yuang2714.simple_text_chat.mcver.extensions;

import net.minecraft.network.chat.Component;

public interface IGuiGraphics {
    int guiWidth();
    int guiHeight();
    void text(Component text, int x, int y, int color);
    void fill(int x0, int y0, int x1, int y1, int color);
    int width(Component text);
    int lineHeight();
}
