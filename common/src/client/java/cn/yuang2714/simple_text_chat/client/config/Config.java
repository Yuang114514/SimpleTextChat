package cn.yuang2714.simple_text_chat.client.config;

import cn.yuang2714.simple_text_chat.client.core.FileUtils;
import com.google.gson.JsonElement;
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
                "modelFolder": "",
                "recognizationMode":"AUTO_DETECT",
                "recognizationLanguage":"ENGLISH",
                "modelDictionary":[],
                "microphoneName":"",
                "sampleRate":44100.0,
                "bufferLength":4096
            }
            """;
    
    private static final String SETUP = "setup";
    private static final String MODEL_FOLDER = "modelFolder";
    private static final String RECOGNIZATION_MODE = "recognizationMode";
    private static final String RECOGNIZATION_LANGUAGE = "recognizationLanguage";
    private static final String MODEL_DICTIONARY = "modelDictionary";
    private static final String MICROPHONE_NAME = "microphoneName";
    private static final String SAMPLE_RATE = "sampleRate";
    private static final String BUFFER_LENGTH = "bufferLength";

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
    
    public static Path getModelStorageFolder() {
        return Path.of(
                get(MODEL_FOLDER, "")
                        .replace(
                                "${GAME_DIR}",
                                FabricLoader.getInstance().getGameDir().toAbsolutePath().toString()
                        )
        );
    }
    
    public static void setModelStorageFolder(Path modelStorageFolder) {
        set(
                MODEL_FOLDER,
                modelStorageFolder
                        .toAbsolutePath()
                        .toString()
                        .replace(
                                FabricLoader.getInstance().getGameDir().toAbsolutePath().toString(),
                                "${GAME_DIR}"
                        )
        );
    }
    
    public static RecognizationMode getMode() {
        return RecognizationMode.valueOf(get(RECOGNIZATION_MODE, RecognizationMode.AUTO_DETECT.name));
    }
    
    public static void setMode(RecognizationMode recognizationMode) {
        set(RECOGNIZATION_MODE, recognizationMode.toString());
    }
    
    public static boolean isSetup() {
        return get(SETUP, false);
    }
    
    public static void setSetup(boolean setup) {
        set(SETUP, setup);
    }
    
    public static ModelLanguage getModelLanguage() {
        return ModelLanguage.valueOf(get(RECOGNIZATION_LANGUAGE, ModelLanguage.CHINESE.name));
    }
    
    public static void setModelLanguage(ModelLanguage language) {
        set(RECOGNIZATION_LANGUAGE, language.toString());
    }
    
    public static Optional<String> getModelDictionary() {
        String value = get(MODEL_DICTIONARY, "[]");
        if (value.equals("[]")) return Optional.empty();
        else return Optional.of(value);
    }
    
    public static void setModelDictionary(String dict) {
        set(MODEL_DICTIONARY, dict);
    }
    
    public static String getMicrophoneName() {
        return get(MICROPHONE_NAME, "DEFAULT");
    }
    
    public static void setMicrophoneName(String name) {
        set(MICROPHONE_NAME, name);
    }
    
    public static float getSampleRate() {
        return get(SAMPLE_RATE, 44100.0f);
    }
    
    public static void setSampleRate(float rate) {
        set(SAMPLE_RATE, rate);
    }
    
    public static int getBufferLength() {
        return get(BUFFER_LENGTH, 8192);
    }
    
    public static void setBufferLength(int length) {
        set(BUFFER_LENGTH, length);
    }
    
    private static <T> T get(String key, T def) {
        try {
            JsonElement element = JsonParser.parseString(FileUtils.read(configFile))
                                            .getAsJsonObject()
                                            .get(key);
            
            if (element == null || element.isJsonNull()) {
                return def;
            }
            
            return (T) switch (def) {
                case String _ -> element.getAsString();
                case Boolean _ -> element.getAsBoolean();
                case Integer _ -> element.getAsInt();
                case Float _ -> element.getAsFloat();
                default -> throw new IllegalStateException("Unexpected type: " + def);
            };
        } catch (Throwable e) {
            LOGGER.warn("Failed to get config {}. returning default value {}.", key, def, e);
            return def;
        }
    }
    
    private static <T> void set(String key, T value) {
        try {
            JsonObject content = JsonParser.parseString(FileUtils.read(configFile)).getAsJsonObject();
            
            if (content.has(key)) {
                content.remove(key);
            }
            
            switch (value) {
                case String s -> content.addProperty(key, s);
                case Boolean b -> content.addProperty(key, b);
                case Integer i -> content.addProperty(key, i);
                case Float f -> content.addProperty(key, f);
                case null, default -> throw new RuntimeException("Unsupported type!");
            }
            
            FileUtils.write(content.toString(), configFile);
        } catch (Throwable e) {
            LOGGER.error("Failed to set into config during setting key {}.", key, e);
        }
    }
}
