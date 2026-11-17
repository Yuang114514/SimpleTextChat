package cn.yuang2714.simple_text_chat.client.minecrafts;

import cn.yuang2714.simple_text_chat.client.config.Config;
import cn.yuang2714.simple_text_chat.client.config.ModelLanguage;
import cn.yuang2714.simple_text_chat.client.config.RecognizationMode;
import cn.yuang2714.simple_text_chat.client.core.MicrophoneManager;
import cn.yuang2714.simple_text_chat.client.mcver.MinecraftVersionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Style;
import org.vosk.Model;

import javax.sound.sampled.Mixer;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static cn.yuang2714.simple_text_chat.SimpleTextChat.LOGGER;

public class ConfigScreen extends Screen {
    private final Screen parentScreen;
    private EditBox modelPath;
    private CycleButton<RecognizationMode> modes;
    private CycleButton<ModelLanguage> languages;
    private CycleButton<Mixer.Info> microphones;
    private Button verify;
    private Button saveAndExit;
    private String lastModelPath;
    private final RecognizationMode lastRecognizationMode;
    private final ModelLanguage lastModelLang;
    private boolean isVerified = true;
    
    public ConfigScreen(Screen parentScreen) {
        super(VersionDifferences.translatableComponent("gui.text_chat.setup.title"));
        this.parentScreen = parentScreen;
        
        RecognizationMode cfgRecognizationMode;
        ModelLanguage cfgModelLang;
        String cfgModelPath;
        try {
            cfgModelLang = Config.getModelLanguage();
            cfgModelPath = Config.getModelStorageFolder().toAbsolutePath().toString();
            cfgRecognizationMode = Config.getMode();
        } catch (Exception ignored) {
            cfgModelPath = "";
            cfgRecognizationMode = RecognizationMode.AUTO_DETECT;
            cfgModelLang = ModelLanguage.CHINESE;
        }
        lastModelLang = cfgModelLang;
        lastRecognizationMode = cfgRecognizationMode;
        lastModelPath = cfgModelPath;
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        super.render(graphics, mouseX, mouseY, a);
        
//        //DEBUG: 原点
//        graphics.fill(
//                START_X,
//                START_Y,
//                START_X + 1,
//                START_Y + 1,
//                0xFF_FF_00_00
//        );
        
        graphics.drawString(
                font,
                VersionDifferences.translatableComponent("gui.text_chat.setup.title"),
                width / 2 - font.width(VersionDifferences.translatableComponent("gui.text_chat.setup.title")) / 2,
                40,
                0xFF_FF_FF_FF,
                true
        );
    }
    
    @Override
    protected void init() {
        //setup layout
        
        //活了1145141919810年才知道的Layout
        //Terry害我不浅啊
        //竖排layout
        LinearLayout layout = LinearLayout.vertical();
        layout.defaultCellSetting().alignHorizontallyCenter().paddingTop(5);
        
        //description
        LinearLayout description = LinearLayout.vertical();
        description.defaultCellSetting().paddingBottom(2).alignHorizontallyCenter();
        
        for (int i = 1; i <= 5; i++) description.addChild(
                new StringWidget(
                        VersionDifferences.translatableComponent("gui.text_chat.setup.description.line_" + i),
                        font
                ));
        
        layout.addChild(description, settings -> settings.paddingBottom(20));
        
        LinearLayout modelLine = LinearLayout.horizontal();
        modelLine.defaultCellSetting().padding(2).alignVerticallyMiddle();
        
        modelLine.addChild(
                new StringWidget(
                        VersionDifferences.translatableComponent("gui.text_chat.setup.model.info"),
                        font
                )
        );
        
        modelPath = new EditBox(
                font,
                0,0,
                295 - font.width(VersionDifferences.translatableComponent("gui.text_chat.setup.model.info")),
                20,
                VersionDifferences.translatableComponent("gui.text_chat.setup.model.info")
        );
        modelPath.setMaxLength(Integer.MAX_VALUE);
        modelPath.setValue(lastModelPath);
        modelPath.setTooltip(Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.setup.model_path.tooltip")));
        modelPath.setResponder(this::onModelPathChange);
        
        modelLine.addChild(modelPath);
        
        modelLine.addChild(
                Button.builder(
                        VersionDifferences.translatableComponent("gui.text_chat.setup.model.download_button_text"),
                        ignored -> MinecraftVersionHelper.HELPER.openUri("https://alphacephei.com/vosk/models")
                ).bounds(
                        0,0,
                        95,
                        20
                ).tooltip(
                        Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.setup.model.tooltip"))
                ).build()
        );
        modelLine.arrangeElements();
        layout.addChild(modelLine);
        
        modes = MinecraftVersionHelper.HELPER.cycleButtonBuilder(
                recognizationMode -> recognizationMode.getName().setStyle(Style.EMPTY.withHoverEvent(
                        MinecraftVersionHelper.HELPER.getShowTextEvent(recognizationMode.getTooltip())
                )), lastRecognizationMode
        ).withValues(
                RecognizationMode.values()
        ).create(
                0,0,
                400,
                20,
                VersionDifferences.translatableComponent("gui.text_chat.setup.mode.info"),
                (button, recognizationMode) ->
                        button.setTooltip(Tooltip.create(recognizationMode.getTooltip()))
        );
        modes.setTooltip(Tooltip.create(lastRecognizationMode.getTooltip()));
        layout.addChild(modes);
        
        languages = MinecraftVersionHelper.HELPER.cycleButtonBuilder(
                ModelLanguage::getName,
                lastModelLang
        ).withValues(
                ModelLanguage.values()
        ).create(
                0,0,
                400,
                20,
                VersionDifferences.translatableComponent("gui.text_chat.setup.lang.info"),
                (button, ignored) ->
                        button.setTooltip(Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.setup.lang.tooltip")))
        );
        languages.setTooltip(Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.setup.lang.tooltip")));
        layout.addChild(languages);
        
        
        List<Mixer.Info> microphoneList = MicrophoneManager.listMixers(true);
        microphones = MinecraftVersionHelper.HELPER.cycleButtonBuilder(
                MicrophoneManager::getDisplayName,
                MicrophoneManager
                        .getMicrophoneFromConfig(microphoneList).orElse(microphoneList.getFirst())
                ).withTooltip(
                        info -> Tooltip.create(
                                MicrophoneManager.getDisplayName(info))
                ).withValues(
                        microphoneList
                ).create(
                        0,0,
                        400,
                        20,
                        VersionDifferences.translatableComponent("gui.text_chat.advanced_config.microphone.name"),
                        (cycleButton, value) -> cycleButton.setTooltip(Tooltip.create(
                                MicrophoneManager.getDisplayName(value)))
                );
        layout.addChild(microphones);
        
        layout.addChild(
                Button.builder(
                        VersionDifferences.translatableComponent("gui.text_chat.advanced_config.title"),
                        ignored -> MinecraftVersionHelper.HELPER.setScreen(new AdvancedConfigScreen(this))
                ).tooltip(
                        Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.advanced_config.warn"))
                ).bounds(
                        0,0,
                        400,
                        20
                ).build()
        );
        
        LinearLayout exitRow = LinearLayout.horizontal();
        exitRow.defaultCellSetting().padding(2);
        
        verify = exitRow.addChild(
                Button.builder(
                        VersionDifferences.translatableComponent("gui.text_chat.setup.verify"),
                        this::verify
                ).tooltip(
                        Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.setup.verify.tooltip"))
                ).bounds(
                        0,0,
                        198,
                        20
                ).build()
        );
        
        saveAndExit = exitRow.addChild(
                Button.builder(
                        VersionDifferences.translatableComponent("gui.text_chat.setup.complete"),
                        this::saveAndExit
                ).tooltip(
                        Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.setup.complete.tooltip"))
                ).bounds(
                        0,0,
                        198,
                        20
                ).build()
        );
        
        exitRow.arrangeElements();
        layout.addChild(exitRow);
        
        layout.arrangeElements();
        layout.visitWidgets(this::addRenderableWidget);
        
        FrameLayout.centerInRectangle(layout, getRectangle());
    }
    
    @Override
    public void tick() {
        if (isVerified) {
            verify.active = false;
            verify.setTooltip(Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.setup.verify.noneed")));
            saveAndExit.active = true;
            saveAndExit.setTooltip(Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.setup.complete.tooltip")));
        } else {
            verify.active = true;
            verify.setTooltip(Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.setup.verify.tooltip")));
            saveAndExit.active = false;
            saveAndExit.setTooltip(Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.setup.complete.tooltip.need_verify")));
        }
    }
    
    @Override
    public void onClose() {
        MinecraftVersionHelper.HELPER.setScreen(parentScreen);
    }
    
    private void verify(Button button) {
        button.setMessage(VersionDifferences.translatableComponent("gui.text_chat.setup.verify.ing"));
        
        try {
            Model model = new Model(modelPath.getValue());
            model.close();
        } catch (IOException e) {
            isVerified = false;
            LOGGER.warn("User entered an invalid path", e);
            button.setMessage(VersionDifferences.translatableComponent("gui.text_chat.setup.verify.error.invalid_path").withStyle(ChatFormatting.RED));
            return;
        }
        
        isVerified = true;
        lastModelPath = modelPath.getValue();
        button.setMessage(VersionDifferences.translatableComponent("gui.text_chat.setup.verify.complete"));
    }
    
    private void saveAndExit(Button button) {
        try {
            Config.setSetup(true);
            Config.setMode(modes.getValue());
            Config.setModelStorageFolder(Paths.get(modelPath.getValue()));
            Config.setModelLanguage(languages.getValue());
            Config.setMicrophoneName(microphones.getValue().getName());
        } catch (Exception e) {
            LOGGER.error("Failed to storage into Config.", e);
            button.setMessage(VersionDifferences.translatableComponent("gui.text_chat.setup.complete.error.failed_to_config"));
            return;
        }
        
        onClose();
    }
    
    private void onModelPathChange(String s) {
        isVerified = s.equals(lastModelPath);
        verify.setMessage(VersionDifferences.translatableComponent("gui.text_chat.setup.verify"));
        
        if (s.contains("cn")) languages.setValue(ModelLanguage.CHINESE);
        else if (s.contains("en")) languages.setValue(ModelLanguage.ENGLISH);
    }
}
