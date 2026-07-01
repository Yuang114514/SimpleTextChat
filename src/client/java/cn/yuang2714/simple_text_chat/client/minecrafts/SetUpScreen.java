package cn.yuang2714.simple_text_chat.client.minecrafts;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.jspecify.annotations.NonNull;

public class SetUpScreen extends Screen {
    private int START_X;
    private int START_Y;
    private final TitleScreen titleScreen;
    
    public SetUpScreen(TitleScreen titleScreen) {
        super(VersionDifferences.translatable("gui.text_chat.setup.title"));
        this.titleScreen = titleScreen;
    }
    
    @Override
    protected void init() {
        START_X = width / 2 - 400 / 2;
        START_Y = height / 2 - 130 / 2;
    }
    
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        graphics.text(
                font,
                VersionDifferences.translatable("gui.text_chat.setup.title"),
                width / 2 - font.width(VersionDifferences.translatable("gui.text_chat.setup.title")) / 2,
                40,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                VersionDifferences.translatable("gui.text_chat.setup.description.line_1"),
                width / 2 - font.width(VersionDifferences.translatable("gui.text_chat.setup.description.line_1")) / 2,
                START_Y + 10,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                VersionDifferences.translatable("gui.text_chat.setup.description.line_2"),
                width / 2 - font.width(VersionDifferences.translatable("gui.text_chat.setup.description.line_2")) / 2,
                START_Y + 20,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                VersionDifferences.translatable("gui.text_chat.setup.description.line_3"),
                width / 2 - font.width(VersionDifferences.translatable("gui.text_chat.setup.description.line_3")) / 2,
                START_Y + 30,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                VersionDifferences.translatable("gui.text_chat.setup.description.line_4"),
                width / 2 - font.width(VersionDifferences.translatable("gui.text_chat.setup.description.line_4")) / 2,
                START_Y + 40,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                VersionDifferences.translatable("gui.text_chat.setup.description.line_5"),
                width / 2 - font.width(VersionDifferences.translatable("gui.text_chat.setup.description.line_5")) / 2,
                START_Y + 50,
                0xFF_FF_FF_FF,
                true
        );
        
//        graphics.text(
//                font,
//                VersionDifferences.translatable("")
//        );
    }
    
    @Override
    public void onClose() {
        super.onClose();
        minecraft.setScreen(titleScreen);
    }
}
