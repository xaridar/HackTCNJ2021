package com.topperbibb.hacktcnj2021.client.config.sprites;

import org.json.JSONObject;

public class Sprite {
    public int x;
    public int y;
    public int h;
    public int w;
    public double probability = 1;
    public double pixelScale = -1;

    public Sprite() {}

    public Sprite(int x, int y, int w, int h, double probability, double pixelScale) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.probability = probability;
        this.pixelScale = pixelScale;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("x", x);
        obj.put("y", y);
        if (w == h) {
            obj.put("sideLength", w);
        } else {
            obj.put("width", w);
            obj.put("height", h);
        }
        if (probability != 1) {
            obj.put("probability", probability);
        }
        if (pixelScale != -1) {
            obj.put("pixelScale", pixelScale);
        }
        return obj;
    }
}
