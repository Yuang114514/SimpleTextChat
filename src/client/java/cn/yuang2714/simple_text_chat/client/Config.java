package cn.yuang2714.simple_text_chat.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static cn.yuang2714.simple_text_chat.SimpleTextChat.LOGGER;

public class Config {
    private static final File configFile = FabricLoader.getInstance().getConfigDir().resolve("simple_text_chat.json").toFile();
    
    public enum Mode {
        HOLD_TO_SPEAK,
        PRESS_SEND,
        AUTO_DETECT
    }

    public static void init() {
        try {
            if (!configFile.exists()) {
                if (configFile.createNewFile()) LOGGER.info("Config file has been created.");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to initialize config system!", e);
            throw new RuntimeException(e);
        }
    }
    
    private JsonObject read() {
        try (Reader reader = new InputStreamReader(new FileInputStream(configFile))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            LOGGER.error("Failed to read config file!", e);
            throw new RuntimeException(e);
        }
    }
    
    private void write(JsonObject text) {
        try (FileOutputStream stream = new FileOutputStream(configFile)) {
            stream.write(text.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Failed to write config file!", e);
            throw new RuntimeException(e);
        }
    }
    
    public Path getModelStorageFolder() {
        return Path.of(read().get("modelFolder").getAsString());
    }
    
    public void setModelStorageFolder(Path modelStorageFolder) {
        JsonObject content = read();
        
        if (content.has("modelFolder")) {
            content.remove("modelFolder");
        }
        
        content.addProperty("modelFolder", modelStorageFolder.toString());
        write(content);
    }
    
    public Mode getMode() {
        return Mode.valueOf(read().get("mode").getAsString());
    }
    
    public void setMode(Mode mode) {
        JsonObject content = read();
        
        if (content.has("mode")) {
            content.remove("mode");
        }
        
        content.addProperty("mode", mode.toString());
        write(content);
    }
}
