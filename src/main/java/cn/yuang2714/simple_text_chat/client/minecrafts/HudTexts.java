package cn.yuang2714.simple_text_chat.client.minecrafts;

import cn.yuang2714.simple_text_chat.client.SimpleTextChatClient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class HudTexts implements IGuiOverlay {
    public volatile static Component text = VersionDifferences.translatable("hud.text_chat.status.disabled").withStyle(ChatFormatting.YELLOW);
    
    public void extractRenderState(GuiGraphics graphics) {
        if (SimpleTextChatClient.isDisabled) return;
        
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
    
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        extractRenderState(guiGraphics);
    }
}
