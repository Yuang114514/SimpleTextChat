package cn.yuang2714.simple_text_chat.client.minecrafts;

import cn.yuang2714.simple_text_chat.client.SimpleTextChatCommon;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class HudTexts {
    public volatile static Component text = VersionDifferences.translatable("hud.text_chat.status.disabled").withStyle(ChatFormatting.YELLOW);
    
    public static void onHudRender(GuiGraphics graphics) {
        if (SimpleTextChatCommon.isDisabled) return;
        
        Minecraft mc = Minecraft.getInstance();
        
        int textWidth = mc.font.width(text);
        int textX = (graphics.guiWidth() - textWidth) - 2;
        int textY = graphics.guiHeight() - 26;
        
        graphics.fill(
                textX - 2,
                textY - 2,
                textX + textWidth + 2,
                textY + mc.font.lineHeight + 2,
                0x80_00_00_00
        );
        
        graphics.drawString(
                mc.font,
                text,
                textX,
                textY,
                0xFF_FF_FF_FF
        );
    }
}
