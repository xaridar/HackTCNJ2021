package com.topperbibb.hacktcnj2021.client.config;

import com.topperbibb.hacktcnj2021.client.game.AudioPlayer;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MusicOption extends JPanel {
    private final JMusicPlayer musicPlayer;
    private String path;

    public MusicOption (JMusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public void setupOption(JFrame parent) {
        JButton chooseMusicButton = new JButton("Choose Song");
        JLabel musicLabel = new JLabel();
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        musicLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    try {
                        musicPlayer.play(AudioPlayer.getInstance(path));
                    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        musicLabel.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
        chooseMusicButton.addActionListener((e) ->  chooseAudioFile(parent, chooseMusicButton, musicLabel));
        chooseMusicButton.setFocusable(false);
        setLayout(new GridLayout(1, 2));
        setMaximumSize(new Dimension(1000, 50));
        add(chooseMusicButton);
        add(musicLabel);
    }

    private void chooseAudioFile(JFrame parent, JButton pressed, JLabel labelToSet) {
        musicPlayer.purge();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("WAV files", "wav"));
        int returnVal = fileChooser.showDialog(parent, "Select");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            path = file.getAbsolutePath();
            pressed.setText("Change File");
            labelToSet.setText(file.getName());
        }
        parent.pack();
    }

    public String getPath() {
        return path;
    }
}
