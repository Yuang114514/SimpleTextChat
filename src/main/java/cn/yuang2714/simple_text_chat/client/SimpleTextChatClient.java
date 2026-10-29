package cn.yuang2714.simple_text_chat.client;

import cn.yuang2714.simple_text_chat.client.config.Config;
import cn.yuang2714.simple_text_chat.client.core.RecognizationManager;
import cn.yuang2714.simple_text_chat.client.minecrafts.ConfigScreen;
import cn.yuang2714.simple_text_chat.client.minecrafts.KeyMappings;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.common.util.Lazy;

public class SimpleTextChatClient {
    public static boolean isDisabled = false;
    
    public static final Lazy<KeyMapping> featureToggleKey = Lazy.of(KeyMappings::featureToggleKey);
    public static final Lazy<KeyMapping> speechDoneKey = Lazy.of(KeyMappings::speechDoneKey);
    public static final Lazy<KeyMapping> speechCancelKey = Lazy.of(KeyMappings::speechCancelKey);
    
	public static void onInitializeClient() {
        System.setProperty("jna.encoding", "UTF-8");
	}
    
    public static void onClientTick() {
        if (isDisabled) return;
        
        if (featureToggleKey.get().consumeClick())
            RecognizationManager.featureToggleAction();
        if (speechDoneKey.get().consumeClick())
            RecognizationManager.onFunctionKeyClick();
        if (speechCancelKey.get().consumeClick())
            RecognizationManager.onCancelKeyClick();
    }
    
    public static void onScreenInit(Minecraft mc, Screen screen) {
        if (isDisabled) return;
        
        try {
            if (screen instanceof TitleScreen titleScreen && !Config.isSetup()) {
                mc.setScreen(new ConfigScreen(titleScreen));
            }
        } catch (Exception ignored) {}
    }
    
    public static void onLevelClear() {
        if (!isDisabled && RecognizationManager.INSTANCE != null) {
            RecognizationManager.INSTANCE.clean();
        }
    }
}