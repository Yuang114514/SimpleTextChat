package cn.yuang2714.simple_text_chat.client.minecrafts;

import cn.yuang2714.simple_text_chat.client.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Util;
import org.jspecify.annotations.NonNull;
import org.vosk.Model;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static cn.yuang2714.simple_text_chat.SimpleTextChat.LOGGER;

public class SetUpScreen extends Screen {
    private int START_X;
    private int START_Y;
    private final Screen parentScreen;
    private EditBox modelPath;
    private CycleButton<Config.Mode> modes;
    
    public SetUpScreen(Screen parentScreen) {
        super(VersionDifferences.translatable("gui.text_chat.setup.title"));
        this.parentScreen = parentScreen;
    }
    
    @Override
    protected void init() {
        START_X = width / 2 - 400 / 2;
        START_Y = height / 2 - 130 / 2;
        
        int modelPathTextWidth = font.width(VersionDifferences.translatable("gui.text_chat.setup.model.info"));
        modelPath = new EditBox(
                font,
                START_X + modelPathTextWidth + 5,
                START_Y + 60,
                295 - modelPathTextWidth,
                20,
                VersionDifferences.translatable("gui.text_chat.setup.model.info")
        );
        modelPath.setMaxLength(Integer.MAX_VALUE);
        
        try {
            if (Config.isSetup()) modelPath.setValue(Config.getModelStorageFolder().toString());
        } catch (Exception _) {}
        
        addRenderableWidget(modelPath);
        
        addRenderableWidget(
                Button.builder(
                        VersionDifferences.translatable("gui.text_chat.setup.model.download_button_text"),
                        _ -> Util.getPlatform().openUri("https://alphacephei.com/vosk/models")
                ).bounds(
                        START_X + 305,
                        START_Y + 60,
                        95,
                        20
                ).tooltip(
                        Tooltip.create(VersionDifferences.translatable("gui.text_chat.setup.model.tooltip"))
                ).build()
        );
        
        modes = CycleButton.builder(
                mode -> mode.getName().setStyle(Style.EMPTY.withHoverEvent(new HoverEvent.ShowText(mode.getTooltip()))),
                Config.Mode.AUTO_DETECT
        ).withValues(
                Config.Mode.values()
        ).create(
                START_X,
                START_Y + 85,
                400,
                20,
                VersionDifferences.translatable("gui.text_chat.setup.mode.info"),
                (button, mode) -> button.setTooltip(Tooltip.create(mode.getTooltip()))
        );
        modes.setTooltip(Tooltip.create(Config.Mode.AUTO_DETECT.getTooltip()));
        addRenderableWidget(modes);
        
        addRenderableWidget(
                Button.builder(
                        VersionDifferences.translatable("gui.text_chat.setup.verify"),
                        this::verify
                ).tooltip(
                        Tooltip.create(VersionDifferences.translatable("gui.text_chat.setup.verify.tooltip"))
                ).bounds(
                        START_X,
                        START_Y + 120,
                        400,
                        20
                ).build()
        );
    }
    
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        
        //DEBUG: 原点
        graphics.fill(
                START_X,
                START_Y,
                START_X + 1,
                START_Y + 1,
                0xFF_FF_00_00
        );
        
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
                START_Y,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                VersionDifferences.translatable("gui.text_chat.setup.description.line_2"),
                width / 2 - font.width(VersionDifferences.translatable("gui.text_chat.setup.description.line_2")) / 2,
                START_Y + 10,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                VersionDifferences.translatable("gui.text_chat.setup.description.line_3"),
                width / 2 - font.width(VersionDifferences.translatable("gui.text_chat.setup.description.line_3")) / 2,
                START_Y + 20,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                VersionDifferences.translatable("gui.text_chat.setup.description.line_4"),
                width / 2 - font.width(VersionDifferences.translatable("gui.text_chat.setup.description.line_4")) / 2,
                START_Y + 30,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                VersionDifferences.translatable("gui.text_chat.setup.description.line_5"),
                width / 2 - font.width(VersionDifferences.translatable("gui.text_chat.setup.description.line_5")) / 2,
                START_Y + 40,
                0xFF_FF_FF_FF,
                true
        );
        
        graphics.text(
                font,
                VersionDifferences.translatable("gui.text_chat.setup.model.info"),
                START_X,
                START_Y + 65,
                0xFF_FF_FF_FF,
                true
        );
    }
    
    @Override
    public void onClose() {
        super.onClose();
        minecraft.setScreen(parentScreen);
    }
    
    public void verify(Button button) {
        Path modelFolder;
        try {
            modelFolder = Paths.get(modelPath.getValue());
            Model model = new Model(modelPath.getValue());
            model.close();
        } catch (IOException e) {
            LOGGER.warn("User entered an invalid path", e);
            button.setMessage(VersionDifferences.translatable("gui.text_chat.setup.verify.error.invalid_path").withStyle(ChatFormatting.RED));
            return;
        }
        
        try {
            Config.setModelStorageFolder(modelFolder);
            Config.setMode(modes.getValue());
            Config.setSetup(true);
        } catch (Exception e) {
            LOGGER.warn("Failed to store into config.", e);
            button.setMessage(VersionDifferences.translatable("gui.text_chat.setup.verify.error.failed_to_config").withStyle(ChatFormatting.RED));
            return;
        }
        
        minecraft.setScreen(parentScreen);
    }
}
