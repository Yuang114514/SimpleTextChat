package cn.yuang2714.simple_text_chat.client.minecrafts;

import cn.yuang2714.simple_text_chat.client.SimpleTextChatClient;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class HudTexts implements HudElement {
    public volatile static Component text = VersionDifferences.translatable("hud.text_chat.status.disabled");
    
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker tracker) {
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
        
        graphics.text(
                mc.font,
                text,
                textX,
                textY,
                0xFF_FF_FF_FF
        );
    }
}
