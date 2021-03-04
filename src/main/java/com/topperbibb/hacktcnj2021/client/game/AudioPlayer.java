package com.topperbibb.hacktcnj2021.client.game;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AudioPlayer {
    public Clip clip;
    public AudioInputStream audioInputStream;
    public String path;

    private static final Set<AudioPlayer> INSTANCES = new HashSet<>();

    public static AudioPlayer getInstance(String file) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if (INSTANCES.stream().noneMatch(audioPlayer -> audioPlayer.path.equals(file))) {
            INSTANCES.add(new AudioPlayer(file));
        }
        return INSTANCES.stream().filter(audioPlayer -> audioPlayer.path.equals(file)).findFirst().orElse(new AudioPlayer(file));
    }

    public AudioPlayer(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.path = path;
        InputStream is = Engine.class.getResourceAsStream("/audio/" + path);
        if (is == null) throw new IOException("Cannot find resource file audio/" + path);
        this.audioInputStream = AudioSystem.getAudioInputStream(is);
        AudioFormat format = audioInputStream.getFormat();

        DataLine.Info info = new DataLine.Info(Clip.class, format);
        clip = (Clip) AudioSystem.getLine(info);
        clip.open(audioInputStream);
    }

    public void startLoop(int nTimes) {
        clip.loop(nTimes);
    }

    public void start() {
        clip.start();
    }

    public void setVolume(int volumePercent) {
        FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float scale = vol.getMaximum() - vol.getMinimum();
        double volAsPercent = volumePercent / 100.0;
        float volOnScale = Math.min(Math.max((float) volAsPercent * scale + vol.getMinimum(), vol.getMinimum()), vol.getMaximum());
        System.out.println(vol.getMinimum());
        System.out.println(vol.getMaximum());
        System.out.println(volAsPercent);
        System.out.println(volOnScale);
        vol.setValue(volOnScale);
    }

    public void startLoop(int nTimes, int volumePercent) {
        setVolume(volumePercent);
        clip.loop(nTimes);
    }

    public void start(int volumePercent) {
        setVolume(volumePercent);
        clip.start();
    }

    public void pause() {
        clip.stop();
    }

    public void discard() {
        clip.flush();
    }

    public void stopClip() {
        clip.stop();
        clip.flush();
    }
}
