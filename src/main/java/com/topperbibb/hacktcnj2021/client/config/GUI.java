package com.topperbibb.hacktcnj2021.client.config;

import com.topperbibb.hacktcnj2021.client.config.audio.JMusicPlayer;
import com.topperbibb.hacktcnj2021.client.config.audio.MusicOption;
import com.topperbibb.hacktcnj2021.client.config.sprites.*;
import com.topperbibb.hacktcnj2021.client.game.Engine;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GUI extends JFrame {

    String spriteSheetPath;
    List<MusicOption> musicOptions = new ArrayList<>();
    ZoomablePannableLabel image;
    JMusicPlayer musicPlayer;
    JPanel addSongPanel;
    JPanel rightPanel;
    private boolean shuffled = false;
    private JCheckBox shuffleCheckBox;

    private MusicOption introOption;
    private final JCheckBox introCheckBox;
    private boolean intro = false;

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
        JPanel rightSide = new JPanel();
        rightPanel = new JPanel();
        JScrollPane scroll = new JScrollPane(rightPanel);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(300, ZoomablePannableLabel.SIZE));
        scroll.setMinimumSize(new Dimension(0, ZoomablePannableLabel.SIZE));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        rightSide.add(scroll);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scroll.setPreferredSize(new Dimension(e.getComponent().getWidth(), ZoomablePannableLabel.SIZE));
                setPreferredSize(new Dimension(ZoomablePannableLabel.SIZE + e.getComponent().getWidth() + 50, getHeight()));
                scroll.repaint();
                scroll.revalidate();
                rightSide.repaint();
                rightSide.revalidate();
            }
        });
        MusicOption mainOption = new MusicOption(musicPlayer);
        mainOption.setupOption(this);

        // Intro
        introCheckBox = new JCheckBox("Add intro song");
        JPanel introPanel = new JPanel();
        introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.Y_AXIS));
        introPanel.add(introCheckBox);
        rightPanel.add(introPanel);
        introCheckBox.setAlignmentX(SwingConstants.WEST);
        introCheckBox.addActionListener((e) -> {
            intro = introCheckBox.isSelected();
            if (intro) {
                if (introOption == null) {
                    introOption = new MusicOption(musicPlayer, "Choose Intro Song", "Change Intro Song");
                    introOption.setupOption(this);
                }
                introPanel.add(introOption);
            } else {
                introPanel.remove(introOption);
            }
            introPanel.repaint();
            introPanel.revalidate();
        });

        musicOptions.add(mainOption);
        rightPanel.add(mainOption);

        // Add song button
        MouseAdapter addSong = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (shuffleCheckBox != null) rightPanel.remove(shuffleCheckBox);
                    rightPanel.remove(addSongPanel);
                    MusicOption option = new MusicOption(musicPlayer);
                    option.setupOption(GUI.this);
                    musicOptions.add(option);
                    rightPanel.add(option);
                    if (musicOptions.size() > 1) {
                        shuffleCheckBox = new JCheckBox("Shuffle songs");
                        shuffleCheckBox.setSelected(shuffled);
                        shuffleCheckBox.setAlignmentX(SwingConstants.WEST);
                        shuffleCheckBox.addActionListener((ev) -> shuffled = shuffleCheckBox.isSelected());
                        rightPanel.add(shuffleCheckBox);
                    }
                    rightPanel.add(addSongPanel);
                    rightPanel.repaint();
                    rightPanel.revalidate();
                    repaint();
                    SwingUtilities.invokeLater(() -> {
                        JScrollBar bar = scroll.getVerticalScrollBar();
                        bar.setValue(bar.getMaximum());
                    });
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


        panel.add(rightSide, BorderLayout.EAST);

        // Image Editor
        image = new ZoomablePannableLabel();

        // Bottom Panel for tiling
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        spinner.addChangeListener(e -> image.setTileSize(((int) spinner.getValue())));
        JLabel tileSizeLabel = new JLabel("Base Tile Size");
        JPanel tileSize = new JPanel();
        tileSize.add(tileSizeLabel);
        tileSize.add(spinner);
        bottomPanel.add(tileSize);

        try {
            JComboBox<SpriteConfig> spritesBox = new JComboBox<>();
            GriddedLabel.SelectionListener listener = selectedArea -> {
                if (image.getTileSize() != 0) image.setSelection(new Rectangle(
                        selectedArea.x / image.getTileSize(),
                        selectedArea.y / image.getTileSize(),
                        selectedArea.width / image.getTileSize(),
                        selectedArea.height / image.getTileSize()
                ));
            };
            spritesBox.addItem(new SpriteConfig("", null));
            SpriteConfig.SpriteSelectListener spriteSelectListener = config -> {
                if (image.getTileSize() == 0) return;
                Sprite s = config.getSprite();
                image.setSelection(
                        new Rectangle(
                                s.x / image.getTileSize(),
                                s.y / image.getTileSize(),
                                s.w / image.getTileSize(),
                                s.h / image.getTileSize()
                        )
                );
                image.setImageLabel();
            };
            Files.readAllLines(Path.of(Engine.class.getResource("/sprites.txt").toURI()))
                    .stream()
                    .map(sprite -> new SpriteConfig(sprite, listener))
                    .forEach(item -> {
                        spritesBox.addItem(item);
                        item.addSpriteSelectListener(spriteSelectListener);
                    });
            image.setListener(area -> {
                IndividualSpriteConfig config = ((SpriteConfig) Objects.requireNonNull(spritesBox.getSelectedItem())).getSelected();
                if (config == null) return;
                Sprite s = config.getSprite();
                s.x = area.x * image.getTileSize();
                s.y = area.y * image.getTileSize();
                s.w = area.width * image.getTileSize();
                s.h = area.height * image.getTileSize();
                config.setUpPanel(((SpriteConfig) spritesBox.getSelectedItem()).randomized);
            });
            spritesBox.setSelectedIndex(0);
            JPanel underPanel = new JPanel();
            spritesBox.addActionListener(e -> {
                underPanel.removeAll();
                underPanel.add(((SpriteConfig) spritesBox.getSelectedItem()));
                assert spritesBox.getSelectedItem() != null;
                if (((SpriteConfig) spritesBox.getSelectedItem()).getSelected() != null && image.getTileSize() != 0) {
                    Sprite s = ((SpriteConfig) spritesBox.getSelectedItem()).getSelected().getSprite();
                    image.setSelection(
                            new Rectangle(
                                    s.x / image.getTileSize(),
                                    s.y / image.getTileSize(),
                                    s.w / image.getTileSize(),
                                    s.h / image.getTileSize()
                            )
                    );
                    image.setImageLabel();
                }
                bottomPanel.repaint();
                bottomPanel.revalidate();
            });
            JPanel spriteBoxPanel = new JPanel();
            JLabel spriteTypeLabel = new JLabel("Sprite Type");
            spriteBoxPanel.add(spriteTypeLabel);
            spriteBoxPanel.add(spritesBox);
            bottomPanel.add(spriteBoxPanel);
            bottomPanel.add(underPanel);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        panel.add(bottomPanel, BorderLayout.SOUTH);


        panel.add(image, BorderLayout.WEST);

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void chooseImageFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")).getParentFile());
        fileChooser.setFileFilter(new FileNameExtensionFilter("images", "png", "jpg", "jpeg", "gif", "tiff", "bmp"));
        int returnVal = fileChooser.showDialog(this, "Select");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            spriteSheetPath = fileChooser.getSelectedFile().getAbsolutePath();
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
                image.setNewIcon(new ImageIcon(i.getScaledInstance(w, h, Image.SCALE_SMOOTH)), i);
                pack();
                image.grabFocus();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new GUI();
    }
}
