package cn.yuang2714.simple_text_chat;

//import cn.yuang2714.simple_text_chat.api.SimpleTextChatRecognizationEvent;
import cn.yuang2714.simple_text_chat.client.SimpleTextChatClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SimpleTextChat.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        SimpleTextChatClient.onClientTick();
    }
    
    @SubscribeEvent
    public static void onClientScreenInit(ScreenEvent.Init event) {
        SimpleTextChatClient.onScreenInit(event.getScreen().getMinecraft(), event.getScreen());
    }
    
    @SubscribeEvent
    public static void onLevelClear(LevelEvent.Unload event) {
        SimpleTextChatClient.onLevelClear();
    }
    
//    @SubscribeEvent
//    public static void partial(SimpleTextChatRecognizationEvent.Partial evt) {
//        SimpleTextChat.LOGGER.info("cached partial {}, {}", evt.getJsonRaw(), evt.getText());
//    }
//
//    @SubscribeEvent
//    public static void result(SimpleTextChatRecognizationEvent.Result evt) {
//        SimpleTextChat.LOGGER.info("cached result {}, {}", evt.getJsonRaw(), evt.getText());
//    }
}
