package cn.yuang2714.simple_text_chat.client.core;

import com.google.gson.JsonParser;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;

public class AudioRecongization {
    void main() throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        
        Model model = new Model("D:\\Soft\\Vosk-JNILibs\\vosk-model-small-cn-0.22");
        
        Recognizer recognizer = new Recognizer(model, 16000.0f);
        
        System.out.println("开始识别");
        captureAudio(recognizer);
    }
    
    public static void captureAudio(Recognizer recognizer) {
        AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        try (TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info)) {
            line.open(format);
            line.start();
            byte[] buffer = new byte[4096];
            while (true) {
                int bytesRead = line.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                        System.out.println("\n一句输出：" +
                                JsonParser.parseString(recognizer.getResult()).getAsJsonObject().get("text").getAsString()
                        );
                    } else {
                        System.out.print("\r临时输出：" +
                                JsonParser.parseString(recognizer.getPartialResult()).getAsJsonObject().get("partial").getAsString()
                        );
                    }
                }
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public static void init() {
    
    }
}
