package cn.yuang2714.simple_text_chat.api;

import cn.yuang2714.simple_text_chat.NoUnusedWarning;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
@NoUnusedWarning
public class SimpleTextChatRecognizationEvent extends Event {
    private final String jsonRaw;
    private final String text;
    
    protected SimpleTextChatRecognizationEvent(String jsonRaw, String text) {
        this.jsonRaw = jsonRaw;
        this.text = text;
    }
    
    @NoUnusedWarning
    public String getJsonRaw() {
        return jsonRaw;
    }
    
    @NoUnusedWarning
    public String getText() {
        return text;
    }
    
    @NoUnusedWarning
    public static class Partial extends SimpleTextChatRecognizationEvent {
        public Partial(String jsonRaw, String text) {
            super(jsonRaw, text);
        }
    }
    
    @NoUnusedWarning
    public static class Result extends SimpleTextChatRecognizationEvent {
        public Result(String jsonRaw, String text) {
            super(jsonRaw, text);
        }
    }
}
