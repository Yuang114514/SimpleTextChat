package cn.yuang2714.simple_text_chat.client;

import cn.yuang2714.simple_text_chat.client.core.FileUtils;
import cn.yuang2714.simple_text_chat.client.minecrafts.VersionDifferences;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.MutableComponent;

import java.io.File;
import java.nio.file.Path;

import static cn.yuang2714.simple_text_chat.SimpleTextChat.LOGGER;

public class Config {
    private static final File configFile = FabricLoader.getInstance().getConfigDir().resolve("simple_text_chat.json").toFile();
    
    public enum Mode {
        HOLD_TO_SPEAK("hold_to_speak"),
        PRESS_SEND("press_send"),
        AUTO_DETECT("auto_detect"),;
        
        public final String name;
        
        Mode(String name) {
            this.name = name;
        }
        
        public MutableComponent getName() {
            return VersionDifferences.translatable("enum.text_chat.modes." + name);
        }
        
        public MutableComponent getTooltip() {
            return VersionDifferences.translatable("enum.text_chat.modes." + name + ".tooltip");
        }
    }

    public static void init() throws Exception {
        try {
            if (!configFile.exists()) {
                if (configFile.createNewFile()) {
                    FileUtils.write("{}", configFile);
                    LOGGER.info("Config file has been created.");
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to initialize config system!", e);
            throw e;
        }
    }
    
    public static Path getModelStorageFolder() throws Exception {
        return Path.of(JsonParser.parseString(FileUtils.read(configFile)).getAsJsonObject().get("modelFolder").getAsString());
    }
    
    public static void setModelStorageFolder(Path modelStorageFolder) throws Exception {
        JsonObject content = JsonParser.parseString(FileUtils.read(configFile)).getAsJsonObject();
        
        if (content.has("modelFolder")) {
            content.remove("modelFolder");
        }
        
        content.addProperty("modelFolder", modelStorageFolder.toString());
        FileUtils.write(content.toString(), configFile);
    }
    
    public static Mode getMode() throws Exception {
        return Mode.valueOf(JsonParser.parseString(FileUtils.read(configFile)).getAsJsonObject().get("mode").getAsString());
    }
    
    public static void setMode(Mode mode) throws Exception {
        JsonObject content = JsonParser.parseString(FileUtils.read(configFile)).getAsJsonObject();
        
        if (content.has("mode")) {
            content.remove("mode");
        }
        
        content.addProperty("mode", mode.toString());
        FileUtils.write(content.toString(), configFile);
    }
    
    public static boolean isSetup() throws Exception {
        try {
            return JsonParser.parseString(FileUtils.read(configFile)).getAsJsonObject().get("setup").getAsBoolean();
        } catch (NullPointerException _) {
            return false;
        }
    }
    
    public static void setSetup(boolean setup) throws Exception {
        JsonObject content = JsonParser.parseString(FileUtils.read(configFile)).getAsJsonObject();
        
        if (content.has("setup")) {
            content.remove("setup");
        }
        
        content.addProperty("setup", setup);
        FileUtils.write(content.toString(), configFile);
    }
}
