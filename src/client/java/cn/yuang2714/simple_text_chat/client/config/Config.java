package cn.yuang2714.simple_text_chat.client.config;

import cn.yuang2714.simple_text_chat.client.core.FileUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import static cn.yuang2714.simple_text_chat.SimpleTextChat.LOGGER;

public class Config {
    private static final File configFile = FabricLoader.getInstance().getConfigDir().resolve("simple_text_chat.json").toFile();
    
    private static final String defaultJson = """
            {
                "setup": false,
                "recognizationMode":"AUTO_DETECT",
                "recognizationLanguage":"ENGLISH",
                "modelDictionary":[]
            }
            """;

    public static void init() throws Exception {
        try {
            if (!configFile.exists()) {
                if (configFile.createNewFile()) {
                    FileUtils.write(defaultJson, configFile);
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
    
    public static RecognizationMode getMode() throws Exception {
        return RecognizationMode.valueOf(JsonParser.parseString(FileUtils.read(configFile)).getAsJsonObject().get("recognizationMode").getAsString());
    }
    
    public static void setMode(RecognizationMode recognizationMode) throws Exception {
        JsonObject content = JsonParser.parseString(FileUtils.read(configFile)).getAsJsonObject();
        
        if (content.has("recognizationMode")) {
            content.remove("recognizationMode");
        }
        
        content.addProperty("recognizationMode", recognizationMode.toString());
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
    
    public static ModelLanguage getModelLanguage() throws Exception {
        return ModelLanguage.valueOf(JsonParser.parseString(FileUtils.read(configFile)).getAsJsonObject().get("recognizationLanguage").getAsString());
    }
    
    public static void setModelLanguage(ModelLanguage language) throws Exception {
        JsonObject content = JsonParser.parseString(FileUtils.read(configFile)).getAsJsonObject();
        
        if (content.has("recognizationLanguage")) {
            content.remove("recognizationLanguage");
        }
        
        content.addProperty("recognizationLanguage", language.toString());
        FileUtils.write(content.toString(), configFile);
    }
    
    public static Optional<String> getModelDictionary() throws Exception {
        String value = JsonParser.parseString(FileUtils.read(configFile)).getAsJsonObject().get("modelDictionary").getAsJsonArray().toString();
        if (value.equals("[]")) return Optional.empty();
        else return Optional.of(value);
    }
}
