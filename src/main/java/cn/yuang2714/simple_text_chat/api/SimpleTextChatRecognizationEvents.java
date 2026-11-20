package cn.yuang2714.simple_text_chat.api;

public class SimpleTextChatRecognizationEvents {
    @FunctionalInterface
    public interface Partial {
        void process(OnText evt);
    }
    
    @FunctionalInterface
    public interface Result {
        void process(OnText evt);
    }
    
    public record OnText(String jsonRaw, String text) {}
}
