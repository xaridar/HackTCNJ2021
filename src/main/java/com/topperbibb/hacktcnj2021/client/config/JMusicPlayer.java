package com.topperbibb.hacktcnj2021.client.config;

import com.topperbibb.hacktcnj2021.client.game.AudioPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class JMusicPlayer extends JPanel {
    private static final int ICON_SIZE = 24;

    public static final Color COLOR_INACTIVE = Color.LIGHT_GRAY;
    public static final Color COLOR_PAUSE_ACTIVE = Color.RED;
    public static final Color COLOR_PLAY_ACTIVE = Color.GREEN;

    private final BufferedImage playImg;
    private final BufferedImage pauseImg;
    private final JPanel panel;
    private AudioPlayer currentlyPlaying;
    private final ProgressBar progressBar;

    private int clipPos = 0;
    private boolean paused = true;

    public JMusicPlayer(BufferedImage playImg, BufferedImage pauseImg) {
        super();
        this.playImg = playImg;
        setColor(playImg, COLOR_INACTIVE);
        this.pauseImg = pauseImg;
        setColor(pauseImg, COLOR_INACTIVE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        progressBar = new ProgressBar(Color.lightGray, Color.darkGray, Color.decode("#1e1e1e"), new Dimension(250, ICON_SIZE));

        progressBar.setSeekListener(new ProgressBar.SeekListener() {
            @Override
            public boolean onSeek(double progress) {
                if (canSeek()) {
                    clipPos = (int) (progress * currentlyPlaying.clip.getFrameLength());
                    currentlyPlaying.clip.setFramePosition(clipPos);
                    return true;
                }
                return false;
            }

            @Override
            public boolean canSeek() {
                return currentlyPlaying != null;
            }
        });

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPanel();
    }

    private void setPanel() {
        JLabel playLabel = new JLabel(new ImageIcon(playImg.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)));
        playLabel.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               if (e.getButton() == MouseEvent.BUTTON1) {
                   unpause();
               }
           }
        });
        playLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        JLabel pauseLabel = new JLabel(new ImageIcon(pauseImg.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)));
        pauseLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    pause();
                }
            }
        });
        pauseLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        panel.removeAll();
        panel.add(playLabel);
        panel.add(progressBar);
        panel.add(pauseLabel);
        remove(panel);
        add(panel);
        repaint();
        revalidate();
    }

    private void setColor(BufferedImage img, Color color) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if (new Color(img.getRGB(x, y), true).getAlpha() != 0) {
                    img.setRGB(x, y, color.getRGB());
                }
            }
        }
    }

    public void play(AudioPlayer player) {
        if (currentlyPlaying != null) currentlyPlaying.stopClip();
        if (updateProgressTimer != null) updateProgressTimer.cancel();
        currentlyPlaying = player;
        clipPos = 0;
        currentlyPlaying.start();
        setColor(pauseImg, COLOR_PAUSE_ACTIVE);
        setColor(playImg, COLOR_INACTIVE);
        setPanel();
        paused = false;
        updateProgressTimer = new Timer();
        updateProgressTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateProgressTask.run();
            }
        }, 0, 100);
    }

    public void pause() {
        if (updateProgressTimer != null) updateProgressTimer.cancel();
        if (currentlyPlaying != null && !paused) {
            paused = true;
            clipPos = currentlyPlaying.clip.getFramePosition();
            progressBar.setProgress((float) clipPos / currentlyPlaying.clip.getFrameLength());
            currentlyPlaying.pause();
            setColor(pauseImg, COLOR_INACTIVE);
            setColor(playImg, COLOR_PLAY_ACTIVE);
            setPanel();
            repaint();
        }
    }

    public void unpause() {
        if (currentlyPlaying != null && paused) {
            paused = false;
            currentlyPlaying.clip.setFramePosition(clipPos);
            currentlyPlaying.start();
            setColor(pauseImg, COLOR_PAUSE_ACTIVE);
            setColor(playImg, COLOR_INACTIVE);
            setPanel();
        }
        updateProgressTimer = new Timer();
        updateProgressTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateProgressTask.run();
            }
        }, 0, 100);
    }

    private final Runnable updateProgressTask = new Runnable() {
        @Override
        public void run() {
            if (currentlyPlaying == null) return;
            if (!currentlyPlaying.clip.isRunning() && !paused) {
                purge();
                return;
            }
            progressBar.setProgress((double) currentlyPlaying.clip.getFramePosition() / currentlyPlaying.clip.getFrameLength());
            progressBar.repaint();
        }
    };

    private java.util.Timer updateProgressTimer;

    public void purge() {
        pause();
        progressBar.setProgress(0);
        progressBar.repaint();
        clipPos = 0;
    }
}
