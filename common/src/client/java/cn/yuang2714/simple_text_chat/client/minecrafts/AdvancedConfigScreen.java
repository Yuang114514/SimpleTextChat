package cn.yuang2714.simple_text_chat.client.minecrafts;

import cn.yuang2714.simple_text_chat.client.config.Config;
import cn.yuang2714.simple_text_chat.client.core.MicrophoneManager;
import cn.yuang2714.simple_text_chat.client.mcver.MinecraftVersionHelper;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import javax.sound.sampled.Mixer;
import java.util.List;
import java.util.stream.StreamSupport;

public class AdvancedConfigScreen extends Screen {
    private final ConfigScreen parent;
    private int startX, startY;
    private MultiLineEditBox dict;
    private CycleButton<Mixer.Info> microphones;
    private EditBox sampleRate;
    private EditBox bufferLength;
    
    public AdvancedConfigScreen(ConfigScreen parent) {
        super(VersionDifferences.translatableComponent("gui.text_chat.advanced_config.title"));
        this.parent = parent;
    }
    
    private static boolean verifyJsonArray(String text) {
        try {
            return StreamSupport.stream(JsonParser.parseString(text).getAsJsonArray().spliterator(), false)
                    .allMatch(element -> element.getAsJsonPrimitive().isString());
        } catch (RuntimeException e) {
            return false;
        }
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        super.render(graphics, mouseX, mouseY, a);
        
        //标题
        graphics.drawString(
                font,
                VersionDifferences.translatableComponent("gui.text_chat.advanced_config.title"),
                (width / 2) - (font.width(VersionDifferences.translatableComponent("gui.text_chat.advanced_config.title")) / 2),
                 startY - font.lineHeight - 20,
                0xFF_FF_FF_FF
        );
        
        graphics.drawString(
                font,
                VersionDifferences.translatableComponent("gui.text_chat.advanced_config.sample_rate"),
                startX,
                startY + 140 - (font.lineHeight / 2),
                0xFF_FF_FF_FF
        );
        
        graphics.drawString(
                font,
                VersionDifferences.translatableComponent("gui.text_chat.advanced_config.buffer_length"),
                startX,
                startY + 165 - (font.lineHeight / 2),
                0xFF_FF_FF_FF
        );
    }
    
    @Override
    public void onClose() {
        MinecraftVersionHelper.HELPER.setScreen(parent);
    }
    
    @Override
    protected void init() {
        startX = width / 2 - 400 / 2;
        startY = height / 2 - 215 / 2;
        
        List<Mixer.Info> microphoneList = MicrophoneManager.listMixers();
        /*
        CycleButton
                .builder(
                        MicrophoneManager::getDisplayName,
                        MicrophoneManager.getMicrophoneFromConfig(microphoneList).orElse(microphoneList.getFirst())
                )
         */
        microphones =
                MinecraftVersionHelper.HELPER.cycleButtonBuilder(
                        MicrophoneManager::getDisplayName,
                        MicrophoneManager.getMicrophoneFromConfig(microphoneList).orElse(microphoneList.getFirst())
                ).withTooltip(
                        info -> Tooltip.create(MicrophoneManager.getDisplayName(info))
                ).withValues(
                        microphoneList
                ).create(
                        startX,
                        startY,
                        400,
                        20,
                        VersionDifferences.translatableComponent("gui.text_chat.advanced_config.microphone.name"),
                        (cycleButton, value) -> cycleButton.setTooltip(Tooltip.create(MicrophoneManager.getDisplayName(value)))
                );
        addRenderableWidget(microphones);
        
        dict = MinecraftVersionHelper.HELPER.createMultiLineEditBox(
                font,
                startX,
                startY + 25,
                400,
                100
        );
        dict.setTooltip(Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.advanced_config.dictionary.tooltip")));
        dict.setValue(Config.getModelDictionary().orElse("[]"));
        addRenderableWidget(dict);
        
        sampleRate = new EditBox(
                font,
                startX + 400 - 75,
                startY + 130,
                75,
                20,
                Component.empty()
        );
        sampleRate.setValue(String.valueOf(Config.getSampleRate()));
        addRenderableWidget(sampleRate);
        
        bufferLength = new EditBox(
                font,
                startX + 400 - 75,
                startY + 155,
                75,
                20,
                Component.empty()
        );
        bufferLength.setValue(String.valueOf(Config.getBufferLength()));
        addRenderableWidget(bufferLength);
        
        addRenderableWidget(
                Button.builder(
                    VersionDifferences.translatableComponent("gui.text_chat.advanced_config.complete"),
                    this::saveAndExit
                ).bounds(
                        startX,
                        startY + 185,
                        400,
                        20
                ).build()
        );
    }
    
    private void saveAndExit(Button button) {
        if (!verifyJsonArray(dict.getValue())) {
            button.setMessage(VersionDifferences.translatableComponent("gui.text_chat.advanced_config.complete.json_syntax_error"));
            return;
        }
        
        Config.setModelDictionary(dict.getValue());
        Config.setMicrophoneName(microphones.getValue().getName());
        
        
        try {
            Config.setSampleRate(Float.parseFloat(sampleRate.getValue()));
            Config.setBufferLength(Integer.parseInt(bufferLength.getValue()));
        } catch (NumberFormatException e) {
            return;
        }
        
        onClose();
    }
}
