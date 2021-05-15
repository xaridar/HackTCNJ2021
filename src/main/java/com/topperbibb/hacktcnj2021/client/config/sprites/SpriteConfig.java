package com.topperbibb.hacktcnj2021.client.config.sprites;

import com.topperbibb.hacktcnj2021.client.game.Engine;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpriteConfig extends JPanel {

    private final String sprite;
    private final MouseAdapter select;
    public boolean randomized;
    private final List<IndividualSpriteConfig> sprites = new ArrayList<>();
    private IndividualSpriteConfig selected;
    private final MouseAdapter addSprite;
    private final List<SpriteSelectListener> selectListeners = new ArrayList<>();

    private JCheckBox randomBox;
    private final ActionListener randomListener = e -> {
        randomized = randomBox.isSelected();
        drawPanel();
    };

    public SpriteConfig(String sprite, GriddedLabel.SelectionListener listener) {
        super();
        this.sprite = sprite;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        if (!sprite.equals("")) {
            sprites.add(new IndividualSpriteConfig(listener));
            selected = sprites.get(0);
        }
        addSprite = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    sprites.add(new IndividualSpriteConfig(listener));
                    select(sprites.get(sprites.size() - 1));
                    drawPanel();
                }
            }
        };
        drawPanel();
        select = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                select(((IndividualSpriteConfig) e.getComponent()));
            }
        };
    }

    private void drawPanel() {
        removeAll();
        sprites.forEach(sprite -> sprite.removeMouseListener(select));
        sprites.forEach(sprite -> sprite.addMouseListener(select));
        if (sprites.size() > 1) {
            randomBox = new JCheckBox("Randomized");
            randomBox.setSelected(randomized);
            randomBox.removeActionListener(randomListener);
            randomBox.addActionListener(randomListener);
            add(randomBox);
        }
        for (IndividualSpriteConfig individualSpriteConfig : sprites) {
            individualSpriteConfig.setUpPanel(randomized);
            add(individualSpriteConfig);
        }
        sprites.forEach(sprite -> sprite.setBorder(null));
        if (getSelected() != null) getSelected().setBorder(BorderFactory.createLineBorder(Color.BLACK));
        try {
            JButton addSpriteButton = new JButton(new ImageIcon(ImageIO.read(Engine.class.getResource("/plus.png")).getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
            addSpriteButton.setBackground(null);
            addSpriteButton.setBorder(null);
            addSpriteButton.setContentAreaFilled(false);
            addSpriteButton.addMouseListener(addSprite);
            addSpriteButton.setFocusPainted(false);
            JPanel addSpritePanel = new JPanel();
            addSpritePanel.setLayout(new BoxLayout(addSpritePanel, BoxLayout.X_AXIS));
            addSpritePanel.add(addSpriteButton);
            JLabel addLabel = new JLabel("Add sprite");
            addSpritePanel.add(addLabel);
            addSpritePanel.addMouseListener(addSprite);
            addSpriteButton.setAlignmentX(CENTER_ALIGNMENT);
            add(addSpritePanel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();
        revalidate();
    }

    @Override
    public String toString() {
        return sprite;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        if (sprites.size() == 1) {
            return sprites.get(0).toJSON();
        }
        if (randomized) obj.put("randomized", true);
        JSONArray arr = new JSONArray();
        sprites.forEach(sprite -> arr.put(sprite.toJSON()));
        obj.put("sprites", arr);
        return obj;
    }

    public void select(IndividualSpriteConfig selected) {
        this.selected = selected;
        drawPanel();
        selectListeners.forEach(listener -> listener.onSelect(selected));
    }

    public IndividualSpriteConfig getSelected() {
        return selected;
    }

    public void addSpriteSelectListener(SpriteSelectListener listener) {
        selectListeners.add(listener);
    }

    public void removeSpriteSelectListener(SpriteSelectListener listener) {
        selectListeners.remove(listener);
    }

    public interface SpriteSelectListener {
        void onSelect(IndividualSpriteConfig config);
    }
}
