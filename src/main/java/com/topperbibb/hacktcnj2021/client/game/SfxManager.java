package com.topperbibb.hacktcnj2021.client.game;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SfxManager {

    public static Map<String, AudioPlayer> sfx = new HashMap<>();

    public static void add(String name, String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        sfx.put(name, AudioPlayer.getInstance(path));
    }

    public static void playSound(String sound) {
        AudioPlayer player = sfx.get(sound);
        if (player == null) return;
        player.start();
    }
}
