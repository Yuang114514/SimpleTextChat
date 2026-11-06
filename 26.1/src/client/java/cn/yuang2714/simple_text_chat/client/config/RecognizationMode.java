package cn.yuang2714.simple_text_chat.client.config;

import cn.yuang2714.simple_text_chat.client.minecrafts.VersionDifferences;
import net.minecraft.network.chat.MutableComponent;

public enum RecognizationMode {
    HOLD_TO_SPEAK("hold_to_speak"),
    PRESS_SEND("press_send"),
    AUTO_DETECT("auto_detect"),;
    
    public final String name;
    
    RecognizationMode(String name) {
        this.name = name;
    }
    
    public MutableComponent getName() {
        return VersionDifferences.translatable("enum.text_chat.modes." + name);
    }
    
    public MutableComponent getTooltip() {
        return VersionDifferences.translatable("enum.text_chat.modes." + name + ".tooltip");
    }
}