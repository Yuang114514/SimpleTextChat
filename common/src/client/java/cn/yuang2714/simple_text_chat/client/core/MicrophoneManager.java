package cn.yuang2714.simple_text_chat.client.core;

import cn.yuang2714.simple_text_chat.SimpleTextChat;
import cn.yuang2714.simple_text_chat.client.config.Config;
import cn.yuang2714.simple_text_chat.client.minecrafts.VersionDifferences;
import net.minecraft.network.chat.Component;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import java.util.*;
import java.util.stream.Collectors;

public class MicrophoneManager {
    public static Mixer.Info EMPTY = new Mixer.Info(
            "MICROPHONE NOT FOUND",
            "MICROPHONE NOT FOUND",
            "MICROPHONE NOT FOUND",
            "MICROPHONE NOT FOUND"
    ) {};
    
    public static List<Mixer.Info> infoCache = new ArrayList<>(List.of(AudioSystem.getMixerInfo()));
    
    public static void refreshCache() {
        infoCache = new ArrayList<>(List.of(AudioSystem.getMixerInfo()));
    }
    
    public static List<Mixer.Info> listMixers(boolean strictForceRealTimeInfo) {
        List<Mixer.Info> rawList = strictForceRealTimeInfo ? List.of(AudioSystem.getMixerInfo()) : infoCache;
        
        //顺便更新缓存
        if (strictForceRealTimeInfo) refreshCache();
        
        StringBuilder rawBuilder = new StringBuilder();
        
        List<Mixer.Info> infoList =
                new ArrayList<>(rawList.stream()
                                      .peek(info -> rawBuilder.append(info.getName()).append("\n"))
                                      .filter(info -> { //只保留麦克风
                                          Mixer mixer = AudioSystem.getMixer(info);
                                          return mixer.getTargetLineInfo().length > 0 && mixer.isLineSupported(RecognizationManager.info);
                                      })
                                      .sorted(Comparator.comparingInt(MicrophoneManager::getSortingSequence))
                                      .toList());
        
        SimpleTextChat.LOGGER.info("Discovering all sound devices... Raw:{}", rawBuilder);
        
        SimpleTextChat.LOGGER.info(
                "Reduced {} non-microphone devices: \n{}",
                rawList.size() - infoList.size(),
                infoList.stream()
                        .map(info -> String.format("[%s] %s | %s", getSortingSequence(info), getDisplayName(info).getString(), info.getName()))
                        .collect(Collectors.joining("\n"))
        );
        
        if (infoList.isEmpty()) infoList.add(EMPTY);
        
        return infoList;
    }
    
    public static Component getDisplayName(Mixer.Info info) {
        if (info == null || info.equals(EMPTY)) return VersionDifferences.translatableComponent("gui.simple_text_chat.microphone.none");
        
        String nameWithoutPortPrefix = info.getName().replaceFirst("^Port ?", "");
        
        String tagKey = switch (getSortingSequence(info)) {
            case 0 -> "default";
            case 1 -> "hardware";
            case 2 -> "virtual";
            case 3 -> "loopback";
            case 4 -> "";
            default -> throw new IllegalStateException("Undefined Sequence!");
        };
        
        if (!tagKey.isEmpty())
            return
                    Component.empty()
                            .append(VersionDifferences.translatableComponent("gui.simple_text_chat.microphone.tag." + tagKey))
                            .append(VersionDifferences.literalComponent(nameWithoutPortPrefix));
        else return VersionDifferences.literalComponent(nameWithoutPortPrefix);
    }
    
    public static int getSortingSequence(Mixer.Info info) {
        int sequence = 4;
        
        String name = info.getName();
        String lowerName = name.toLowerCase();
        
        if (lowerName.contains("cable") || lowerName.contains("virtual")) {
            sequence = 2;
        } else if (lowerName.contains("立体声混音") || lowerName.contains("stereo mix")) {
            sequence = 3;
        } else if (name.contains("主") || lowerName.contains("main")) {
            sequence = 0;
        } else if (name.startsWith("Port") || info.getClass().getSimpleName().equals("DirectAudioDeviceInfo")) {
            sequence = 1;
        }
        
        return sequence;
    }
    
    public static Optional<Mixer.Info> getMicrophoneFromConfig(List<Mixer.Info> mixers) {
        String microphoneName = Config.getMicrophoneName();
        
        if (mixers.isEmpty()) return Optional.empty();
        
        return mixers.stream()
                     .filter(info -> Objects.equals(microphoneName, info.getName()))
                     .findFirst()
                     .or(() -> {
                         SimpleTextChat.LOGGER.warn("Selected microphone not found, falling back to default. Configured: {}", microphoneName);
                         return Optional.of(mixers.getFirst());
                     });
    }
    
    public static Mixer getMixer() {
        List<Mixer.Info> infoList = listMixers(true);
        
        return AudioSystem.getMixer(getMicrophoneFromConfig(infoList).orElseGet(() -> {
            //第一个必是默认
            SimpleTextChat.LOGGER.warn("Configured microphone not found, selecting default.");
            return infoList.getFirst();
        }));
    }
}
