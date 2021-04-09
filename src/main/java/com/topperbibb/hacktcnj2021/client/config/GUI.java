package com.topperbibb.hacktcnj2021.client.config;

import com.topperbibb.hacktcnj2021.client.game.Engine;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {

    String spritesheetPath;
    List<MusicOption> musicOptions = new ArrayList<>();
    ZoomablePannableLabel image;
    JMusicPlayer musicPlayer;
    JPanel addSongPanel;
    JPanel rightPanel;

    public GUI() throws IOException {
        super("Sprite Sheet Config");
        getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());

        add(panel, BorderLayout.CENTER);

        // Menu
        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem chooseImage = new JMenuItem("Choose File");
        chooseImage.addActionListener(e -> chooseImageFile());
        fileMenu.add(chooseImage);
        bar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");

        bar.add(editMenu);

        getContentPane().add(bar, BorderLayout.NORTH);

        // Audio Player
        musicPlayer = new JMusicPlayer(ImageIO.read(Engine.class.getResource("/play.png")), ImageIO.read(Engine.class.getResource("/pause.png")));
        panel.add(musicPlayer, BorderLayout.NORTH);

        // Right-side config
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        MusicOption mainOption = new MusicOption(musicPlayer);
        mainOption.setupOption(this);
        musicOptions.add(mainOption);
        rightPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        rightPanel.add(mainOption);

        // Add song button

        MouseAdapter addSong = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    rightPanel.remove(addSongPanel);
                    MusicOption option = new MusicOption(musicPlayer);
                    option.setupOption(GUI.this);
                    musicOptions.add(option);
                    rightPanel.add(option);
                    rightPanel.add(addSongPanel);
                    rightPanel.repaint();
                    rightPanel.revalidate();
                    repaint();
                }
            }
        };

        JButton addSongButton = new JButton(new ImageIcon(ImageIO.read(Engine.class.getResource("/plus.png")).getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        addSongButton.setBackground(null);
        addSongButton.setBorder(null);
        addSongButton.setContentAreaFilled(false);
        addSongButton.addMouseListener(addSong);
        addSongButton.setFocusPainted(false);
        addSongPanel = new JPanel();
        addSongPanel.setLayout(new BoxLayout(addSongPanel, BoxLayout.X_AXIS));
        addSongPanel.add(addSongButton);
        addSongPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 110));
        JLabel addLabel = new JLabel("Add song");
        addSongPanel.add(addLabel);
        addSongPanel.addMouseListener(addSong);
        rightPanel.add(addSongPanel);


        panel.add(rightPanel, BorderLayout.EAST);

        // Image Editor
        image = new ZoomablePannableLabel();

        panel.add(image, BorderLayout.WEST);

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void chooseImageFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("images", "png", "jpg", "jpeg", "gif", "tiff", "bmp"));
        int returnVal = fileChooser.showDialog(this, "Select");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            spritesheetPath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                int w, h;
                BufferedImage i = ImageIO.read(fileChooser.getSelectedFile());
                if (i.getWidth() >= i.getHeight()) {
                    w = ZoomablePannableLabel.SIZE;
                    h = (int) (((double) ZoomablePannableLabel.SIZE / i.getWidth()) * i.getHeight());
                } else {
                    h = ZoomablePannableLabel.SIZE;
                    w = (int) (((double) ZoomablePannableLabel.SIZE / i.getHeight()) * i.getWidth());
                }
                image.setNewIcon(new ImageIcon(i.getScaledInstance(w, h, Image.SCALE_SMOOTH)));
                pack();
                image.grabFocus();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        GUI gui = new GUI();
    }
}
