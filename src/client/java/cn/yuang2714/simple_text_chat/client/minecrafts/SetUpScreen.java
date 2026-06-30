package cn.yuang2714.simple_text_chat.client.minecrafts;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class SetUpScreen extends Screen {
    private final int START_X;
    private final int START_Y;
    
    public SetUpScreen() {
        super(Component.translatable("gui.text_chat.setup.title"));
        START_X = width / 2 - 400 / 2;
        START_Y = height / 2 - 130 / 2;
    }
    
    @Override
    protected void init() {
    
    }
    
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        graphics.text(
                font,
                Component.translatable("gui.text_chat.setup.title"),
                width / 2 - font.width(Component.translatable("gui.text_chat.setup.title")) / 2,
                40,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                Component.translatable("gui.text_chat.setup.description.line_1"),
                width / 2 - font.width(Component.translatable("gui.text_chat.setup.description.line_1")) / 2,
                START_Y + 10,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                Component.translatable("gui.text_chat.setup.description.line_2"),
                width / 2 - font.width(Component.translatable("gui.text_chat.setup.description.line_2")),
                START_Y + 20,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                Component.translatable("gui.text_chat.setup.description.line_3"),
                width / 2 - font.width(Component.translatable("gui.text_chat.setup.description.line_3")),
                START_Y + 30,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                Component.translatable("gui.text_chat.setup.description.line_4"),
                width / 2 - font.width(Component.translatable("gui.text_chat.setup.description.line_4")),
                START_Y + 40,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                Component.translatable("gui.text_chat.setup.description.line_5"),
                width / 2 - font.width(Component.translatable("gui.text_chat.setup.description.line_5")),
                START_Y + 50,
                0xFF_FF_FF_FF,
                true
        );
    }
}
