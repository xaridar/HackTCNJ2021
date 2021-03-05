package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.Client;
import com.topperbibb.hacktcnj2021.client.game.user.MovableUser;
import com.topperbibb.hacktcnj2021.client.game.user.NetUser;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * Main class, which will start a game in either single- or multi-player mode
 */
public class Game {
    public static void main(String[] args) {
         createSinglePlayer();
    }

    /**
     * Creates a new {@link MovableUser} and attaches it to a single player {@link Engine} instance
     */
    private static void createSinglePlayer() {
        MovableUser u = new MovableUser();
        Engine.startSinglePlayerEngine(u);
        try {
            AudioPlayer introPlayer = AudioPlayer.getInstance("DeathIsKeyIntro.wav");
            introPlayer.addFinishListener(() -> {
                try {
                    AudioPlayer.getInstance("DeathIsKeySong.wav").startLoop(Clip.LOOP_CONTINUOUSLY, 85);
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            });
            introPlayer.start(85);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a {@link Client} instance and tries to connect it to the server
     */
    public static void createMultiplayer() {
        NetUser u = new NetUser();
        Client c = new Client("127.0.0.1", 443, u);
        u.connect(c);
        c.connect();
    }
}
