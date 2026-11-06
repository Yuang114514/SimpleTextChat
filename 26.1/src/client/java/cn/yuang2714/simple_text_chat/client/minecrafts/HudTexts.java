package cn.yuang2714.simple_text_chat.client.minecrafts;

import cn.yuang2714.simple_text_chat.client.SimpleTextChatClient;
import cn.yuang2714.simple_text_chat.mcver.extensions.IGuiGraphics;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class HudTexts {
    public volatile static Component text = VersionDifferences.translatable("hud.text_chat.status.disabled").withStyle(ChatFormatting.YELLOW);
    
    public void render(IGuiGraphics graphics) {
        if (SimpleTextChatClient.isDisabled) return;
        
        int textWidth = graphics.width(text);
        int textX = (graphics.guiWidth() - textWidth) - 2;
        int textY = graphics.guiHeight() - 26;
        
        graphics.fill(
                textX - 2,
                textY - 2,
                textX + textWidth + 2,
                textY + graphics.lineHeight() + 2,
                0x80_00_00_00
        );
        
        graphics.text(
                text,
                textX,
                textY,
                0xFF_FF_FF_FF
        );
    }
}
