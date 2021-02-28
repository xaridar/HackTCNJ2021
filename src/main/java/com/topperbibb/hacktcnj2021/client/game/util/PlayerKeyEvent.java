package com.topperbibb.hacktcnj2021.client.game.util;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayerKeyEvent extends KeyEvent {
    public PlayerKeyEvent(Component source, int id, long when, int modifiers, int keyCode, char keyChar, int keyLocation) {
        super(source, id, when, modifiers, keyCode, keyChar, keyLocation);
    }

    public PlayerKeyEvent(KeyEvent e) {
        super(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getKeyCode(), e.getKeyChar(), e.getKeyLocation());
    }


}
