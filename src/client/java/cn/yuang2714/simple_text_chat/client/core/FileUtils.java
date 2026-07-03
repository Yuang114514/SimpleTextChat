package cn.yuang2714.simple_text_chat.client.core;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static cn.yuang2714.simple_text_chat.SimpleTextChat.LOGGER;

public class FileUtils {
    public static String read(File file) throws Exception {
        try (Reader reader = new InputStreamReader(new FileInputStream(file))) {
            return reader.readAllAsString();
        } catch (IOException e) {
            LOGGER.error("Failed to read file {}", file, e);
            throw e;
        }
    }
    
    public static void write(String text, File file) throws Exception {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(text.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Failed to write file {}", file, e);
            throw e;
        }
    }
}
