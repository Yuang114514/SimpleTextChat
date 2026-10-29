package cn.yuang2714.simple_text_chat.client.config;

import cn.yuang2714.simple_text_chat.client.minecrafts.VersionDifferences;
import net.minecraft.network.chat.MutableComponent;

public enum ModelLanguage {
    CHINESE("chinese"),
    ENGLISH("english");
    
    public final String name;
    
    ModelLanguage(String name) {
        this.name = name;
    }
    
    public MutableComponent getName() {
        return VersionDifferences.translatable("enum.text_chat.langs." + name);
    }
}
