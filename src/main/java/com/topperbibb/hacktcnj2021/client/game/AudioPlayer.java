package com.topperbibb.hacktcnj2021.client.game;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@SuppressWarnings("unused")
public class AudioPlayer {
    public Clip clip;
    public AudioInputStream audioInputStream;
    public String path;
    public Set<AudioFinishListener> afterPlay = new HashSet<>();

    private static final Set<AudioPlayer> INSTANCES = new HashSet<>();

    public static AudioPlayer getInstance(String file) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if (INSTANCES.stream().noneMatch(audioPlayer -> audioPlayer.path.equals(file))) {
            INSTANCES.add(new AudioPlayer(file));
        }
        return INSTANCES.stream().filter(audioPlayer -> audioPlayer.path.equals(file)).findFirst().orElse(new AudioPlayer(file));
    }

    private AudioPlayer(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.path = path;
        InputStream is = Engine.class.getResourceAsStream("/audio/" + path);
        if (is == null) throw new IOException("Cannot find resource file audio/" + path);
        this.audioInputStream = AudioSystem.getAudioInputStream(is);
        AudioFormat format = audioInputStream.getFormat();

        DataLine.Info info = new DataLine.Info(Clip.class, format);
        clip = (Clip) AudioSystem.getLine(info);
        clip.open(audioInputStream);
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                clip.setFramePosition(0);
                if (afterPlay != null) {
                    afterPlay.forEach(AudioFinishListener::onFinish);
                }
            }
        });
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

    public void playThen(AudioFinishListener afterPlay) {
        addFinishListener(afterPlay);
        start();
    }

    public void removeFinishListener(AudioFinishListener listener) {
        afterPlay.remove(listener);
    }

    public void addFinishListener(AudioFinishListener listener) {
        afterPlay.add(listener);
    }

    public static void playQueue(AudioPlayer... players) {
        for (int i = 0; i < players.length - 1; i++) {
            final int finalI = i;
            players[i].addFinishListener(new AudioFinishListener() {
                @Override
                public void onFinish() {
                    players[finalI + 1].start();
                    players[finalI].removeFinishListener(this);
                }
            });
        }
        players[0].start();
    }

    public static void playShuffle(AudioPlayer... players) {
        AudioPlayer[] temp = new AudioPlayer[players.length];
        Random rand = new Random();
        List<AudioPlayer> tempList = Arrays.asList(players);
        Collections.shuffle(tempList);
        tempList.toArray(temp);
        playQueue(temp);
        temp[temp.length - 1].addFinishListener(new AudioFinishListener() {
            @Override
            public void onFinish() {
                playShuffle(players);
                temp[temp.length - 1].removeFinishListener(this);
            }
        });
    }

    public interface AudioFinishListener {
        void onFinish();
    }
}
