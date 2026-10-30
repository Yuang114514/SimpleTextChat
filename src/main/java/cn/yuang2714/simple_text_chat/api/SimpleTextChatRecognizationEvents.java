package cn.yuang2714.simple_text_chat.api;

import cn.yuang2714.simple_text_chat.NoUnusedWarning;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@NoUnusedWarning
public interface SimpleTextChatRecognizationEvents {
    @NoUnusedWarning
    Event<Partial> PARTIAL_EVENT = EventFactory.createArrayBacked(
            Partial.class,
            listeners -> evt -> {
                for (Partial listener : listeners) listener.process(evt);
            }
    );
    
    Event<Result> RESULT_EVENT = EventFactory.createArrayBacked(
            Result.class,
            listeners -> evt -> {
                for (Result listener : listeners) listener.process(evt);
            }
    );
    
    @FunctionalInterface
    interface Partial {
        void process(OnText evt);
    }
    
    @FunctionalInterface
    interface Result {
        void process(OnText evt);
    }
    
    record OnText(String jsonRaw, String text) {}
}
