package cn.yuang2714.simple_text_chat.client.core;

import java.io.File;
import java.nio.file.Files;

public class FileUtils {
    public static String read(File file) throws Exception {
        return Files.readString(file.toPath());
    }
    
    public static void write(String text, File file) throws Exception {
        Files.writeString(file.toPath(), text);
    }
}
