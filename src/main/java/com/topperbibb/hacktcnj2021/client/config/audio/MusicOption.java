package com.topperbibb.hacktcnj2021.client.config.audio;

import com.topperbibb.hacktcnj2021.client.config.audio.JMusicPlayer;
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
    private final String msg;
    private final String changeMessage;

    public MusicOption (JMusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
        msg = "Choose Song";
        changeMessage = "Change Song";
    }

    public MusicOption (JMusicPlayer musicPlayer, String startMessage, String changeMessage) {
        this.musicPlayer = musicPlayer;
        msg = startMessage;
        this.changeMessage = changeMessage;
    }

    public void setupOption(JFrame parent) {
        JButton chooseMusicButton = new JButton(msg);
        JLabel musicLabel = new JLabel();
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        musicLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && !musicLabel.getText().equals("")) {
                    try {
                        musicPlayer.play(AudioPlayer.getInstance(path));
                    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        musicLabel.setMaximumSize(new Dimension(150, 40));
        musicLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        chooseMusicButton.addActionListener((e) ->  chooseAudioFile(parent, chooseMusicButton, musicLabel));
        chooseMusicButton.setFocusable(false);
        setLayout(new GridLayout(1, 2));
        setMaximumSize(new Dimension(300, 40));
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
            pressed.setText(changeMessage);
            String nameToShow = file.getName();
            if (nameToShow.length() > 22) {
                nameToShow = nameToShow.substring(0, 19) + "...";
            }
            labelToSet.setText(nameToShow);
        }
        parent.pack();
    }

    public String getPath() {
        return path;
    }
}
