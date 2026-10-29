package cn.yuang2714.simple_text_chat.client.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.util.Optional;

public class Config {
    public static Path getModelStorageFolder() {
        return Path.of(HOLDER.modelStoragePath.get());
    }
    
    public static void setModelStorageFolder(Path modelStorageFolder) {
        HOLDER.modelStoragePath.set(modelStorageFolder.toFile().getAbsolutePath());
    }
    
    public static RecognizationMode getMode() {
        return HOLDER.mode.get();
    }
    
    public static void setMode(RecognizationMode recognizationMode) {
        HOLDER.mode.set(recognizationMode);
    }
    
    public static boolean isSetup() {
        return HOLDER.isSetup.get();
    }
    
    public static void setSetup(boolean setup) {
        HOLDER.isSetup.set(setup);
    }
    
    public static ModelLanguage getModelLanguage() {
        return HOLDER.lang.get();
    }
    
    public static void setModelLanguage(ModelLanguage language) {
        HOLDER.lang.set(language);
    }
    
    public static Optional<String> getModelDictionary() {
        String value = HOLDER.modelDictionary.get();
        if (value.equals("[]")) return Optional.empty();
        else return Optional.of(value);
    }
    
    public static void setModelDictionary(String dictionary) {
        HOLDER.modelDictionary.set(dictionary);
    }
    
    public static final Holder HOLDER;
    public static final ForgeConfigSpec CONFIG_SPEC;
    
    static {
        final Pair<Holder, ForgeConfigSpec> specPair =
                new ForgeConfigSpec.Builder().configure(Holder::new);
        
        HOLDER = specPair.getLeft();
        CONFIG_SPEC = specPair.getRight();
    }
    
    public static class Holder {
        public final ForgeConfigSpec.ConfigValue<String> modelStoragePath;
        public final ForgeConfigSpec.EnumValue<RecognizationMode> mode;
        public final ForgeConfigSpec.BooleanValue isSetup;
        public final ForgeConfigSpec.EnumValue<ModelLanguage> lang;
        public final ForgeConfigSpec.ConfigValue<String> modelDictionary;
        
        Holder(ForgeConfigSpec.Builder builder) {
            builder.comment("Configs for Simple Text Chat").push("Config");
            
            modelStoragePath = builder
                    .comment("The storage folder of recognization model.")
                            .define("modelStoragePath", "");
            
            mode = builder
                    .comment("Recognization Mode.")
                            .defineEnum("recognizationMode", RecognizationMode.AUTO_DETECT);
            
            isSetup = builder
                    .comment("Whether mod has setup.")
                            .define("isSetup", false);
            
            lang = builder
                    .comment("Recognization Language.")
                            .defineEnum("recognizationLanguage", ModelLanguage.CHINESE);
            
            modelDictionary = builder
                    .comment("The dictionary of model, formatted in JSON List format.\n" +
                                     "WARNING: If you use small models, it will REPLACE the result by dictionary," +
                                     " and large models won't work with dictionary.")
                            .define("modelDictionary", "[]");
            
            builder.pop();
        }
    }
}
