package cn.yuang2714.simple_text_chat.client.minecrafts;

import cn.yuang2714.simple_text_chat.client.config.Config;
import cn.yuang2714.simple_text_chat.client.mcver.MinecraftVersionHelper;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.stream.StreamSupport;

public class AdvancedConfigScreen extends Screen {
    private final ConfigScreen parent;
    private int startX, startY;
    private MultiLineEditBox dict;
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
    public void render(GuiGraphics graphics, int i, int j, float f) {
        super.render(graphics, i, j, f);
        
        graphics.drawString(
                font,
                VersionDifferences.translatableComponent("gui.text_chat.advanced_config.title"),
                width / 2 - font.width(VersionDifferences.translatableComponent("gui.text_chat.advanced_config.title")) / 2,
                40,
                0xFF_FF_FF_FF
        );
    }
    
    @Override
    public void onClose() {
        MinecraftVersionHelper.HELPER.setScreen(parent);
    }
    
    private void saveAndExit(Button button) {
        if (!verifyJsonArray(dict.getValue())) {
            button.setMessage(VersionDifferences.translatableComponent("gui.text_chat.advanced_config.complete.json_syntax_error"));
            return;
        }
        
        Config.setModelDictionary(dict.getValue());
        
        try {
            Config.setSampleRate(Float.parseFloat(sampleRate.getValue()));
            Config.setBufferLength(Integer.parseInt(bufferLength.getValue()));
        } catch (NumberFormatException e) {
            return;
        }
        
        onClose();
    }
    
    @Override
    protected void init() {
        startX = width / 2 - 400 / 2;
        startY = height / 2 - 215 / 2;
        
        dict = MinecraftVersionHelper.HELPER.createMultiLineEditBox(
                font,
                startX,
                startY,
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
        sampleRate.setTooltip(Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.advanced_config.sample_rate.tooltip")));
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
}
