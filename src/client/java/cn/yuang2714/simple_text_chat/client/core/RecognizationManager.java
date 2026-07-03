package cn.yuang2714.simple_text_chat.client.core;

import cn.yuang2714.simple_text_chat.SimpleTextChat;
import cn.yuang2714.simple_text_chat.client.Config;
import cn.yuang2714.simple_text_chat.client.SimpleTextChatClient;
import cn.yuang2714.simple_text_chat.client.minecrafts.HudTexts;
import cn.yuang2714.simple_text_chat.client.minecrafts.VersionDifferences;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;

import static cn.yuang2714.simple_text_chat.SimpleTextChat.LOGGER;

public class RecognizationManager extends Thread {
    public static RecognizationManager INSTANCE;
    
    private final Model model;
    private final Recognizer recognizer;
    private final Config.Mode mode;
    
    private static final AudioFormat audioFormat = new AudioFormat(16000.0f, 16, 1, true, false);
    private static final DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
    
    private short recordingStatus = 1; //1 代表正在录音，2 代表录音完成等待发送，3 代表等待下一次录音
    private volatile String partialResult;
    private volatile String result;
    private volatile boolean isFinished = false;
    
    public static class PressEvent {
        private volatile boolean hasEvent = false;
        
        public void submit() {
            hasEvent = true;
        }
        
        public boolean isConsumable() {
            return hasEvent;
        }
        
        public void consume() {
            hasEvent = false;
        }
        
        public PressEvent() {}
    }
    public static volatile PressEvent functionKey = new PressEvent();
    public static volatile PressEvent cancelKey = new PressEvent();
    
    public RecognizationManager() throws Exception {
        super();
        
        LOGGER.info("Initializing Recognization Manager.");
        
        model = new Model(Config.getModelStorageFolder().toString());
        recognizer = new Recognizer(model, 16000.0f);
        mode = Config.getMode();
        
        Thread recognizationThread = new Thread(this::recognize);
        recognizationThread.setName("Audio Recognization Thread");
        recognizationThread.start();
        
        if (mode == Config.Mode.HOLD_TO_SPEAK) recordingStatus = 3;
        
        setName("Recognization Manager Thread");
        
        HudTexts.text = VersionDifferences.translatable("hud.text_chat.status.ready").withStyle(ChatFormatting.GREEN);
        LOGGER.info("Recognization Manager Started. Mode: {}", mode);
    }
    
    public static void setup() {
        if (SimpleTextChatClient.isDisabled) return;
        
        HudTexts.text = VersionDifferences.translatable("hud.text_chat.status.preparing").withStyle(ChatFormatting.GREEN);
        
        new Thread(() -> {
            try {
                INSTANCE = new RecognizationManager();
            } catch (Exception e) {
                LOGGER.error("Failed to initialize Recognization Manager.", e);
                HudTexts.text = VersionDifferences.translatable("hud.text_chat.status.setup_failed").withStyle(ChatFormatting.RED);
            }
            INSTANCE.start();
        }, "Recognization Manager Initializer").start();
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (true) {
            if (isFinished) {
                return;
            }
            
            switch (mode) {
                case AUTO_DETECT -> intervalInAutoDetectMode();
                case PRESS_SEND -> intervalInPressSendMode();
                case HOLD_TO_SPEAK -> intervalInHoldToSpeakMode();
            }
            
            LOGGER.debug("Processing interval for mode: {}", mode);
            
            try {
                Thread.sleep(250);
            } catch (InterruptedException _) {}
        }
    }
    
    private void commonInterval() {
        if (partialResult == null || partialResult.isEmpty()) {
            partialResult = " ";
        }
        
        HudTexts.text = VersionDifferences.literal(partialResult);
        
        LOGGER.debug("Synchronized Partial Result ‘{}’ to HUD.", partialResult);
    }
    
    private void intervalInAutoDetectMode() {
        commonInterval();
        if (result != null) {
            sendPlayerChat(result);
            result = null;
        }
    }
    
    private void intervalInPressSendMode() {
        commonInterval();
        if (functionKey.isConsumable()) {
            sendPlayerChat(selectResult());
            functionKey.consume();
        }
    }
    
    private void intervalInHoldToSpeakMode() {
        commonInterval();
        if (functionKey.isConsumable()) {
            if (recordingStatus == 3) {
                recordingStatus = 1;
            } else if (recordingStatus == 1) {
                recordingStatus = 2;
            }
            functionKey.consume();
        }
        
        if (recordingStatus == 2) {
            sendPlayerChat(selectResult());
            recordingStatus = 3;
        }
    }
    
    private String selectResult() {
        if (!partialResult.equals(" ")) {
            return partialResult;
        } else if (result != null && !result.isEmpty()) {
            return result;
        } else {
            return " ";
        }
    }
    
    private void sendPlayerChat(String message) {
        if (message == null || message.isBlank() || message.equals("我")) return;
        if (cancelKey.isConsumable()) {
            cancelKey.consume();
            return;
        }
        
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            localPlayer.connection.sendChat(message);
        } else LOGGER.warn("Unable to get LocalPlayer.");
    }
    
    public void clean() {
        LOGGER.info("Cleaning up Recognization Manager.");
        isFinished = true;
        HudTexts.text = VersionDifferences.translatable("hud.text_chat.status.disabled");
        
        LOGGER.info("Recognization Manager cleaned.");

        INSTANCE = null;
    }
    
    private void recognize() {
        try (TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info)) {
            line.open(audioFormat);
            line.start();
            
            byte[] buffer = new byte[4096];
            while (true) {
                if (isFinished) {
                    recognizer.close();
                    model.close();
                    LOGGER.info("Finished Recognization Manager.");
                    return;
                }
                if (recordingStatus != 1) continue;
                
                LOGGER.debug("Writing {} bytes into Recognizer.", buffer.length);
                int bytesRead = line.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                        result = processVoskResult(recognizer.getResult());
                    } else {
                        partialResult = processVoskResult(recognizer.getPartialResult());
                    }
                }
            }
        } catch (LineUnavailableException e) {
            SimpleTextChat.LOGGER.error("Audio line not available.", e);
            HudTexts.text = VersionDifferences.translatable("hud.text_chat.status.line_unavailable").withStyle(ChatFormatting.RED);
        }
    }
    
    private static String processVoskResult(String result) {
        JsonObject resultAsJsonObject = JsonParser.parseString(result).getAsJsonObject();
        
        String temp;
        if (resultAsJsonObject.has("text")) {
            temp = resultAsJsonObject.get("text").getAsString();
        } else if (resultAsJsonObject.has("partial")) {
            temp = resultAsJsonObject.get("partial").getAsString();
        } else {
            temp = "";
        }
        
        return temp.replace(" ", "");
    }
}
