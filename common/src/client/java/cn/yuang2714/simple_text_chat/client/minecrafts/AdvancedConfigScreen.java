package cn.yuang2714.simple_text_chat.client.minecrafts;

import cn.yuang2714.simple_text_chat.client.config.Config;
import cn.yuang2714.simple_text_chat.client.mcver.MinecraftVersionHelper;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.stream.StreamSupport;

public class AdvancedConfigScreen extends Screen {
    private final ConfigScreen parent;
    private MultiLineEditBox dict;
    private EditBox sampleRate;
    private EditBox bufferLength;
    
    public AdvancedConfigScreen(ConfigScreen parent) {
        super(AllTranslations.GUI_ADVANCED_CONFIG_TITLE);
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
                AllTranslations.GUI_ADVANCED_CONFIG_TITLE,
                (width / 2) - (font.width(AllTranslations.GUI_ADVANCED_CONFIG_TITLE) / 2),
                40,
                0xFF_FF_FF_FF
        );
    }
    
    @Override
    public void onClose() {
        MinecraftVersionHelper.HELPER.setScreen(parent);
    }
    
    @Override
    protected void init() {
        LinearLayout layout = LinearLayout.vertical();
        layout.defaultCellSetting().paddingTop(5).alignHorizontallyCenter();
        
        dict = MinecraftVersionHelper.HELPER.createMultiLineEditBox(
                font,
                0,0,
                400,
                100
        );
        dict.setTooltip(Tooltip.create(VersionDifferences.translatableComponent("gui.text_chat.advanced_config.dictionary.tooltip")));
        dict.setTooltip(Tooltip.create(AllTranslations.GUI_ADVANCED_CONFIG_DICTIONARY_TOOLTIP));
        dict.setValue(Config.getModelDictionary().orElse("[]"));
        layout.addChild(dict);
        
        LinearLayout sampleRateLine = LinearLayout.horizontal();
        sampleRateLine.defaultCellSetting().alignVerticallyMiddle().padding(0);
        
        sampleRateLine.addChild(new StringWidget(AllTranslations.GUI_ADVANCED_CONFIG_SAMPLE_RATE, font));
        
        sampleRateLine.addChild(new SpacerElement(400 - 75 - font.width(AllTranslations.GUI_ADVANCED_CONFIG_SAMPLE_RATE), 0), settings -> settings.padding(0));
        
        sampleRate = new EditBox(
                font,
                0,0,
                75,
                20,
                Component.empty()
        );
        sampleRate.setTooltip(Tooltip.create(AllTranslations.GUI_ADVANCED_CONFIG_SAMPLE_RATE_TOOLTIP));
        sampleRate.setValue(String.valueOf(Config.getSampleRate()));
        sampleRateLine.addChild(sampleRate);
        
        sampleRateLine.arrangeElements();
        layout.addChild(sampleRateLine);
        
        LinearLayout bufferLengthLine = LinearLayout.horizontal();
        bufferLengthLine.defaultCellSetting().alignVerticallyMiddle();
        
        bufferLengthLine.addChild(new StringWidget(AllTranslations.GUI_ADVANCED_CONFIG_BUFFER_LENGTH, font));
        
        bufferLengthLine.addChild(new SpacerElement(400 - 75 - font.width(
                AllTranslations.GUI_ADVANCED_CONFIG_BUFFER_LENGTH), 0), settings -> settings.padding(0));
        
        bufferLength = new EditBox(
                font,
                0,0,
                75,
                20,
                Component.empty()
        );
        bufferLength.setValue(String.valueOf(Config.getBufferLength()));
        bufferLengthLine.addChild(bufferLength);
        
        bufferLengthLine.arrangeElements();
        layout.addChild(bufferLengthLine);
        
        layout.addChild(
                Button.builder(
                        AllTranslations.GUI_ADVANCED_CONFIG_COMPLETE,
                        this::saveAndExit
                ).bounds(
                        0,0,
                        400,
                        20
                ).build()
        );
        
        layout.arrangeElements();
        layout.visitWidgets(this::addRenderableWidget);
        
        FrameLayout.centerInRectangle(layout, getRectangle());
    }
    
    private void saveAndExit(Button button) {
        if (!verifyJsonArray(dict.getValue())) {
            button.setMessage(AllTranslations.GUI_ADVANCED_CONFIG_COMPLETE_JSON_SYNTAX_ERROR);
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
}
