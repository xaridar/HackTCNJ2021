package com.topperbibb.hacktcnj2021.client.config.sprites;

import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class IndividualSpriteConfig extends JPanel {

    private final Sprite sprite;
    private final ChangeListener changeListener;

    private JSpinner x;
    private JSpinner y;
    private JSpinner w;
    private JSpinner h;

    public IndividualSpriteConfig(GriddedLabel.SelectionListener listener) {
        super();
        changeListener = e -> listener.onSelect(new Rectangle((int) x.getValue(), (int) y.getValue(), (int) w.getValue(), (int) h.getValue()));
        sprite = new Sprite();
        setUpPanel(false);
    }

    public void setUpPanel(boolean randomized) {
        removeAll();
        x = new JSpinner(new SpinnerNumberModel(sprite.x, 0, 10000, 1));
        x.setPreferredSize(new Dimension(50, 20));
        JLabel xLabel = new JLabel("x");
        y = new JSpinner(new SpinnerNumberModel(sprite.y, 0, 10000, 1));
        y.setPreferredSize(new Dimension(50, 20));
        JLabel yLabel = new JLabel("y");
        w = new JSpinner(new SpinnerNumberModel(sprite.w, 0, 10000, 1));
        w.setPreferredSize(new Dimension(50, 20));
        JLabel wLabel = new JLabel("w");
        h = new JSpinner(new SpinnerNumberModel(sprite.h, 0, 10000, 1));
        h.setPreferredSize(new Dimension(50, 20));
        JLabel hLabel = new JLabel("h");
        add(xLabel);
        add(x);
        add(yLabel);
        add(y);
        add(wLabel);
        add(w);
        add(hLabel);
        add(h);

        if (randomized) {
            JSpinner prob = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
            prob.setPreferredSize(new Dimension(50, 20));
            JLabel probLabel = new JLabel("Probability");
            add(probLabel);
            add(prob);
        }

        x.addChangeListener(changeListener);
        y.addChangeListener(changeListener);
        w.addChangeListener(changeListener);
        h.addChangeListener(changeListener);
        repaint();
        revalidate();
    }

    public JSONObject toJSON() {
        return sprite.toJSON();
    }

    public Sprite getSprite() {
        return sprite;
    }

}
