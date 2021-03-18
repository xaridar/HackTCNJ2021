package com.topperbibb.hacktcnj2021.client.game;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * A player for a single audio clip
 */
public class AudioPlayer {
    // The java.sound.sampled.Clip that holds the audio stream
    public Clip clip;

    // The audio stream read from the file
    public AudioInputStream audioInputStream;

    // The resource path to the desired clip
    public String path;

    // A HashSet of AudioFinishListeners, which are called upon completion of a clip
    public Set<AudioFinishListener> afterPlay = new HashSet<>();

    // A static HashSet containing all created AudioPlayer instances, each unique for a specific file
    private static final Set<AudioPlayer> INSTANCES = new HashSet<>();

    /**
     * Statically returns the appropriate instance for an audio clip, or creates a new one.
     * @param file the resource path of the desired path
     * @return a singleton AudioPlayer instance based on {@code file}
     * @throws IOException if the file path cannot be found
     * @throws UnsupportedAudioFileException if the file is of improper type
     * @throws LineUnavailableException if there is an issue reading from the file
     */
    public static AudioPlayer getInstance(String file) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if (INSTANCES.stream().noneMatch(audioPlayer -> audioPlayer.path.equals(file))) {
            INSTANCES.add(new AudioPlayer(file));
        }
        return INSTANCES.stream().filter(audioPlayer -> audioPlayer.path.equals(file)).findFirst().orElse(new AudioPlayer(file));
    }

    /**
     * Returns a new AudioPlayer from a given audio file
     * @param path the resource path of the desired path
     * @throws IOException if the file path cannot be found
     * @throws UnsupportedAudioFileException if the file is of improper type
     * @throws LineUnavailableException if there is an issue reading from the file
     */
    private AudioPlayer(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.path = path;
        InputStream is = Engine.class.getResourceAsStream("/audio/" + path);
        if (is == null) throw new IOException("Cannot find resource file audio/" + path);
        if (path.endsWith(".wav"))
            this.audioInputStream = AudioSystem.getAudioInputStream(is);
        else {

        }
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

    /**
     * Starts a playback loop
     * @param nTimes the number of times to loop a clip - Use {@link Clip#LOOP_CONTINUOUSLY} for infinite looping
     */
    public void startLoop(int nTimes) {
        clip.loop(nTimes);
    }

    /**
     * Starts a clip to run once
     */
    public void start() {
        clip.start();
    }

    /**
     * Sets the volume of a clip based on a percentage
     * @param volumePercent the percent at which the volume should be set, as a double between 0 and 100
     */
    public void setVolume(double volumePercent) {
        FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float scale = vol.getMaximum() - vol.getMinimum();
        double volAsPercent = volumePercent / 100;
        float volOnScale = Math.min(Math.max((float) volAsPercent * scale + vol.getMinimum(), vol.getMinimum()), vol.getMaximum());
        vol.setValue(volOnScale);
    }

    /**
     * Starts a loop at a specified volume
     * @param nTimes the number of times to loop a clip - Use {@link Clip#LOOP_CONTINUOUSLY} for infinite looping
     * @param volumePercent the percent at which the volume should be set, as a double between 0 and 100
     */
    public void startLoop(int nTimes, int volumePercent) {
        setVolume(volumePercent);
        clip.loop(nTimes);
    }

    /**
     * Starts a clip at a specified volume
     * @param volumePercent the percent at which the volume should be set, as a double between 0 and 100
     */
    public void start(int volumePercent) {
        setVolume(volumePercent);
        clip.start();
    }

    /**
     * Pauses a clip for later playback
     */
    public void pause() {
        clip.stop();
    }

    /**
     * Discards the clip and its saved position
     */
    public void discard() {
        clip.flush();
    }

    /**
     * Stops a clip and discards its saved position
     */
    public void stopClip() {
        clip.stop();
        clip.flush();
    }

    /**
     * Plays a clip, and attaches a {@link AudioFinishListener} for after it finishes
     * @param afterPlay the callback for after a sile finishes playing
     */
    public void playThen(AudioFinishListener afterPlay) {
        addFinishListener(afterPlay);
        start();
    }

    /**
     * Adds an AudioFinishListener to {@link #afterPlay}
     * @param listener the AudioFinishListener to add
     */
    public void addFinishListener(AudioFinishListener listener) {
        afterPlay.add(listener);
    }

    /**
     * Removes an AudioFinishListener from {@link #afterPlay}
     * @param listener the AudioFinishListener to remove
     */
    public void removeFinishListener(AudioFinishListener listener) {
        afterPlay.remove(listener);
    }

    /**
     * Statically plays an array of AudioPlayers consecutively, in the order of the passed array
     * @param players an array of AudioPlayers to play
     */
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

    /**
     * Plays an array of AudioPlayers in a random order continuously
     * @param players an unordered array of AudioPlayers to shuffle
     */
    public static void playShuffle(AudioPlayer... players) {
        AudioPlayer[] temp = new AudioPlayer[players.length];
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

    /**
     * Closes all open audio input streams
     */
    public static void closePlayers() {
        INSTANCES.forEach(player -> {
            try {
                player.audioInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * An audio event listener called after an AudioPlayer has finished playing a clip, or finished looping when in loop mode
     */
    public interface AudioFinishListener {
        void onFinish();
    }
}
