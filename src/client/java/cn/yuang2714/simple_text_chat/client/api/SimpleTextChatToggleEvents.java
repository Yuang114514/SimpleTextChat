package cn.yuang2714.simple_text_chat.client.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.lang.annotation.Documented;

/**
 *
 */
public class SimpleTextChatToggleEvents {
    public static final Event<On> ON = EventFactory.createArrayBacked(
            On.class,
            ons -> () -> {
                for (On evt : ons) {
                    evt.on();
                }
            }
    );
    
    public static final Event<Off> OFF = EventFactory.createArrayBacked(
            Off.class,
            offs -> () -> {
                for (Off evt : offs) {
                    evt.off();
                }
            }
    );
    
    @FunctionalInterface
    public interface On { void on(); }
    
    @FunctionalInterface
    public interface Off { void off(); }
}
