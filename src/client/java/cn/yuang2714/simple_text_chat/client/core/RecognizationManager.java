package cn.yuang2714.simple_text_chat.client.core;

import cn.yuang2714.simple_text_chat.SimpleTextChat;
import cn.yuang2714.simple_text_chat.client.config.Config;
import cn.yuang2714.simple_text_chat.client.SimpleTextChatClient;
import cn.yuang2714.simple_text_chat.client.config.RecognizationMode;
import cn.yuang2714.simple_text_chat.client.minecrafts.HudTexts;
import cn.yuang2714.simple_text_chat.client.minecrafts.VersionDifferences;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;

import static cn.yuang2714.simple_text_chat.SimpleTextChat.LOGGER;

public class RecognizationManager extends Thread {
    public static volatile RecognizationManager INSTANCE;
    public static volatile boolean isEnabled = false;
    private static volatile boolean hasActiveInitializer = false;
    
    private final Model model;
    private final Recognizer recognizer;
    private final RecognizationMode recognizationMode;
    private final String modelName;
    
    private static final AudioFormat audioFormat = new AudioFormat(16000.0f, 16, 1, true, false);
    private static final DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
    
    private volatile boolean isRecording = true;
    private volatile String partialResult;
    private volatile String result;
    private volatile boolean isFinished = false;
    private boolean hasCursed = false;
    private volatile boolean needCancel = false;
    private final Thread recognizationThread;
    
    public RecognizationManager() throws Exception {
        super();
        
        LOGGER.info("Initializing Recognization Manager.");
        
        model = new Model(Config.getModelStorageFolder().toString());
        recognizer = new Recognizer(model, 16000.0f);
        recognizationMode = Config.getMode();
        modelName = Config.getModelStorageFolder().getFileName().toString();
        
        recognizationThread = new Thread(this::recognize);
        recognizationThread.setName("Audio Recognization Thread");
        recognizationThread.start();
        
        if (recognizationMode == RecognizationMode.HOLD_TO_SPEAK) isRecording = false;
        
        setName("Recognization Manager Thread");
        
        HudTexts.text = VersionDifferences.translatable("hud.text_chat.status.ready").withStyle(ChatFormatting.GREEN);
        LOGGER.info("Recognization Manager Started. RecognizationMode: {}", recognizationMode);
    }
    
    public static void setup() {
        if (SimpleTextChatClient.isDisabled || INSTANCE != null || hasActiveInitializer) return;
        hasActiveInitializer = true;
        
        String model = "ERROR";
        Component mode = VersionDifferences.translatable("ERROR");
        try {
            model = Config.getModelStorageFolder().getFileName().toString();
            mode = Config.getMode().getName();
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Recognization Manager.", e);
            HudTexts.text = VersionDifferences.translatable("hud.text_chat.status.setup_failed").withStyle(ChatFormatting.RED);
        }
        
        HudTexts.text = VersionDifferences.translatable("hud.text_chat.status.preparing", model, mode).withStyle(ChatFormatting.GREEN);
        
        new Thread(() -> {
            try {
                INSTANCE = new RecognizationManager();
            } catch (Exception e) {
                LOGGER.error("Failed to initialize Recognization Manager.", e);
                HudTexts.text = VersionDifferences.translatable("hud.text_chat.status.setup_failed").withStyle(ChatFormatting.RED);
                hasActiveInitializer = false;
                return;
            }
            
            if (!isEnabled) {
                INSTANCE.clean();
                hasActiveInitializer = false;
                return;
            }
            
            INSTANCE.start();
            hasActiveInitializer = false;
        }, "Recognization Manager Initializer").start();
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (true) {
            if (isFinished) {
                return;
            }
            
            interval();
            
            LOGGER.debug("Processing interval for recognizationMode: {}", recognizationMode);
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException _) {}
        }
    }
    
    private void interval() {
        if (partialResult == null || partialResult.isEmpty()) {
            partialResult = " ";
        }
        
        HudTexts.text = VersionDifferences.translatable(
                "hud.text_chat.status.rec_msg",
                (
                        isRecording
                        ? VersionDifferences.translatable("hud.text_chat.status.ing", addCursor(partialResult))
                        : VersionDifferences.translatable("hud.text_chat.status.ready")
                ),
                modelName,
                recognizationMode.getName()
        );
        
        LOGGER.debug("Synchronized Partial Result ‘{}’ to HUD.", partialResult);
    }
    
    private String addCursor(String s) {
        if (hasCursed) s += "|";
        
        hasCursed = !hasCursed;
        
        return s;
    }
    
    public static void onFunctionKeyClick() {
        if (INSTANCE == null) return;
        
        INSTANCE.onFunctionKeyClickInternal();
    }
    
    private void onFunctionKeyClickInternal() {
        switch (recognizationMode) {
            case AUTO_DETECT -> {}
            case PRESS_SEND -> sendPlayerChat(selectResult());
            case HOLD_TO_SPEAK -> {
                if (isRecording) {
                    sendPlayerChat(selectResult());
                    partialResult = "";
                    result = "";
                    isRecording = false;
                } else {
                    result = "";
                    partialResult = "";
                    isRecording = true;
                }
            }
        }
    }
    
    public static void onCancelKeyClick() {
        if (INSTANCE == null) return;
        
        INSTANCE.needCancel = true;
    }
    
    public static synchronized void featureToggleAction() {
        isEnabled = !isEnabled;
        
        if (isEnabled) {
            RecognizationManager.setup();
        } else {
            if (RecognizationManager.INSTANCE != null) RecognizationManager.INSTANCE.clean();
            HudTexts.text = VersionDifferences.translatable("hud.text_chat.status.disabled").withStyle(ChatFormatting.YELLOW);
        }
    }
    
    private String selectResult() {
        if (mode == Config.Mode.AUTO_DETECT) {
            if (result != null && ! result.isEmpty()) {
                return result;
            } else if (partialResult != null && ! partialResult.equals(" ")) {
                return partialResult;
            } else {
                return " ";
            }
        } else {
            if (partialResult != null && ! partialResult.isBlank() && ! partialResult.isEmpty()) {
                return partialResult;
            } else if (result != null && ! result.isBlank() && ! result.isEmpty()) {
                return result;
            } else {
                return " ";
            }
        }
    }
    
    private void sendPlayerChat(String message) {
        if (message == null || message.isBlank() || message.equals("我")) return;
        if (needCancel) {
            needCancel = false;
            return;
        }
        
        Minecraft.getInstance().execute(() -> {
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer != null) {
                localPlayer.connection.sendChat(message);
            } else LOGGER.warn("Unable to get LocalPlayer.");
        });
    }
    
    public void clean() {
        LOGGER.info("Cleaning up Recognization Manager.");
        isFinished = true;
        HudTexts.text = VersionDifferences.translatable("hud.text_chat.status.disabled");
        
        try {
            recognizationThread.join();
            if (Thread.currentThread() != this || getState() != State.NEW) join();
        } catch (InterruptedException _) {}
        
        LOGGER.info("Recognization Manager cleaned.");

        INSTANCE = null;
    }
    
    @SuppressWarnings("BusyWait")
    private void recognize() {
        try (TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info)) {
            line.open(audioFormat);
            line.start();
            
            byte[] buffer = new byte[4096];
            while (true) {
                if (isFinished) return;
                if (!isRecording) {
                    try { Thread.sleep(100); } catch (InterruptedException _) {}
                    continue;
                }
                
                LOGGER.debug("Writing {} bytes into Recognizer.", buffer.length);
                int bytesRead = line.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    boolean isFinal = recognizer.acceptWaveForm(buffer, bytesRead);
                    if (recognizationMode != RecognizationMode.HOLD_TO_SPEAK) {
                        if (isFinal) {
                            result = processVoskResult(recognizer.getResult());
                            if (recognizationMode == RecognizationMode.AUTO_DETECT) sendPlayerChat(result);
                        } else {
                            partialResult = processVoskResult(recognizer.getPartialResult());
                        }
                    } else {
                        if (isFinal) {
                            String lastResult = result;
                            result = (lastResult == null ? "" : lastResult) + processVoskResult(recognizer.getResult());
                        } else {
                            partialResult = processVoskResult(recognizer.getPartialResult());
                        }
                    }
                }
            }
        } catch (LineUnavailableException e) {
            SimpleTextChat.LOGGER.error("Audio line not available.", e);
            HudTexts.text = VersionDifferences.translatable("hud.text_chat.status.line_unavailable").withStyle(ChatFormatting.RED);
        } finally {
            recognizer.close();
            model.close();
            LOGGER.info("Finished Recognization Manager.");
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
