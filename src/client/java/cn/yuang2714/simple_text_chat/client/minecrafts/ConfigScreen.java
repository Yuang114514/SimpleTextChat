package cn.yuang2714.simple_text_chat.client.minecrafts;

import cn.yuang2714.simple_text_chat.client.config.Config;
import cn.yuang2714.simple_text_chat.client.config.ModelLanguage;
import cn.yuang2714.simple_text_chat.client.config.RecognizationMode;
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
import java.nio.file.Paths;

import static cn.yuang2714.simple_text_chat.SimpleTextChat.LOGGER;

public class ConfigScreen extends Screen {
    private int START_X;
    private int START_Y;
    private final Screen parentScreen;
    private EditBox modelPath;
    private CycleButton<RecognizationMode> modes;
    private CycleButton<ModelLanguage> languages;
    private Button verify;
    private Button saveAndExit;
    private String lastModelPath;
    private final RecognizationMode lastRecognizationMode;
    private final ModelLanguage lastModelLang;
    private boolean isVerified = true;
    
    public ConfigScreen(Screen parentScreen) {
        super(VersionDifferences.translatable("gui.text_chat.setup.title"));
        this.parentScreen = parentScreen;
        
        RecognizationMode cfgRecognizationMode;
        ModelLanguage cfgModelLang;
        String cfgModelPath;
        try {
            cfgModelLang = Config.getModelLanguage();
            cfgModelPath = Config.getModelStorageFolder().toAbsolutePath().toString();
            cfgRecognizationMode = Config.getMode();
        } catch (Exception _) {
            cfgModelPath = "";
            cfgRecognizationMode = RecognizationMode.AUTO_DETECT;
            cfgModelLang = ModelLanguage.CHINESE;
        }
        lastModelLang = cfgModelLang;
        lastRecognizationMode = cfgRecognizationMode;
        lastModelPath = cfgModelPath;
    }
    
    @Override
    protected void init() {
        START_X = width / 2 - 400 / 2;
        START_Y = height / 2 - 150 / 2;
        
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
        modelPath.setValue(lastModelPath);
        modelPath.setTooltip(Tooltip.create(VersionDifferences.translatable("gui.text_chat.setup.model_path.tooltip")));
        modelPath.setResponder(this::onModelPathChange);
        
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
                recognizationMode -> recognizationMode.getName().setStyle(Style.EMPTY.withHoverEvent(new HoverEvent.ShowText(
                        recognizationMode.getTooltip()))),
                lastRecognizationMode
        ).withValues(
                RecognizationMode.values()
        ).create(
                START_X,
                START_Y + 85,
                400,
                20,
                VersionDifferences.translatable("gui.text_chat.setup.mode.info"),
                (button, recognizationMode) ->
                        button.setTooltip(Tooltip.create(recognizationMode.getTooltip()))
        );
        modes.setTooltip(Tooltip.create(lastRecognizationMode.getTooltip()));
        addRenderableWidget(modes);
        
        languages = CycleButton.builder(
                ModelLanguage::getName,
                lastModelLang
        ).withValues(
                ModelLanguage.values()
        ).create(
                START_X,
                START_Y + 110,
                400,
                20,
                VersionDifferences.translatable("gui.text_chat.setup.lang.info"),
                (button, _) ->
                        button.setTooltip(Tooltip.create(VersionDifferences.translatable("gui.text_chat.setup.lang.tooltip")))
        );
        languages.setTooltip(Tooltip.create(VersionDifferences.translatable("gui.text_chat.setup.lang.tooltip")));
        addRenderableWidget(languages);
        
        verify = addRenderableWidget(
                Button.builder(
                        VersionDifferences.translatable("gui.text_chat.setup.verify"),
                        this::verify
                ).tooltip(
                        Tooltip.create(VersionDifferences.translatable("gui.text_chat.setup.verify.tooltip"))
                ).bounds(
                        START_X,
                        START_Y + 140,
                        198,
                        20
                ).build()
        );
        
        saveAndExit = addRenderableWidget(
                Button.builder(
                        VersionDifferences.translatable("gui.text_chat.setup.complete"),
                        this::saveAndExit
                ).tooltip(
                        Tooltip.create(VersionDifferences.translatable("gui.text_chat.setup.complete.tooltip"))
                ).bounds(
                        START_X + 200,
                        START_Y + 140,
                        198,
                        20
                ).build()
        );
    }
    
    @Override
    public void tick() {
        if (isVerified) {
            verify.active = false;
            verify.setTooltip(Tooltip.create(VersionDifferences.translatable("gui.text_chat.setup.verify.noneed")));
            saveAndExit.active = true;
            saveAndExit.setTooltip(Tooltip.create(VersionDifferences.translatable("gui.text_chat.setup.complete.tooltip")));
        } else {
            verify.active = true;
            verify.setTooltip(Tooltip.create(VersionDifferences.translatable("gui.text_chat.setup.verify.tooltip")));
            saveAndExit.active = false;
            saveAndExit.setTooltip(Tooltip.create(VersionDifferences.translatable("gui.text_chat.setup.complete.tooltip.need_verify")));
        }
    }
    
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        
//        //DEBUG: 原点
//        graphics.fill(
//                START_X,
//                START_Y,
//                START_X + 1,
//                START_Y + 1,
//                0xFF_FF_00_00
//        );
        
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
        minecraft.gui.setScreen(parentScreen);
    }
    
    private void verify(Button button) {
        button.setMessage(VersionDifferences.translatable("gui.text_chat.setup.verify.ing"));
        
        try {
            Model model = new Model(modelPath.getValue());
            model.close();
        } catch (IOException e) {
            isVerified = false;
            LOGGER.warn("User entered an invalid path", e);
            button.setMessage(VersionDifferences.translatable("gui.text_chat.setup.verify.error.invalid_path").withStyle(ChatFormatting.RED));
            return;
        }
        
        isVerified = true;
        lastModelPath = modelPath.getValue();
        button.setMessage(VersionDifferences.translatable("gui.text_chat.setup.verify.complete"));
    }
    
    private void saveAndExit(Button button) {
        try {
            Config.setSetup(true);
            Config.setMode(modes.getValue());
            Config.setModelStorageFolder(Paths.get(modelPath.getValue()));
            Config.setModelLanguage(languages.getValue());
        } catch (Exception e) {
            LOGGER.error("Failed to storage into Config.", e);
            button.setMessage(VersionDifferences.translatable("gui.text_chat.setup.complete.error.failed_to_config"));
            return;
        }
        
        minecraft.gui.setScreen(parentScreen);
    }
    
    private void onModelPathChange(String s) {
        isVerified = s.equals(lastModelPath);
        verify.setMessage(VersionDifferences.translatable("gui.text_chat.setup.verify"));
        
        if (s.contains("cn")) languages.setValue(ModelLanguage.CHINESE);
        else if (s.contains("en")) languages.setValue(ModelLanguage.ENGLISH);
    }
}
