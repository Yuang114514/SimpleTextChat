package cn.yuang2714.simple_text_chat;

import cn.yuang2714.simple_text_chat.client.SimpleTextChatClient;
import cn.yuang2714.simple_text_chat.client.config.Config;
import cn.yuang2714.simple_text_chat.client.minecrafts.ConfigScreen;
import cn.yuang2714.simple_text_chat.client.minecrafts.HudTexts;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.yuang2714.simple_text_chat.SimpleTextChat.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SimpleTextChat {
	public static final String MOD_ID = "text_chat";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("SimpleTextChat");
    
    public SimpleTextChat(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.CLIENT, Config.CONFIG_SPEC);
        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> new ConfigScreen(screen))
        );
        SimpleTextChatClient.onInitializeClient();
    }
    
    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(SimpleTextChatClient.featureToggleKey.get());
        event.register(SimpleTextChatClient.speechCancelKey.get());
        event.register(SimpleTextChatClient.speechDoneKey.get());
    }
    
    @SubscribeEvent
    public static void onRegisterHUD(RegisterGuiOverlaysEvent event) {
        event.registerAbove(
                VanillaGuiOverlay.CHAT_PANEL.id(),
                "recognization_info",
                new HudTexts()
        );
    }
    
//    @SubscribeEvent
//    @SuppressWarnings("removal")
//    public static void onClientSetup(FMLClientSetupEvent event) {
//        event.enqueueWork(() -> ModLoadingContext.get().registerExtensionPoint(
//                ConfigScreenHandler.ConfigScreenFactory.class,
//                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> new ConfigScreen(screen))
//        ));
//    }
}
