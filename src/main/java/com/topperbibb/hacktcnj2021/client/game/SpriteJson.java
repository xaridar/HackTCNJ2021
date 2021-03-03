package com.topperbibb.hacktcnj2021.client.game;

import com.topperbibb.hacktcnj2021.client.game.graphics.FlipEnum;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteManager;
import com.topperbibb.hacktcnj2021.client.game.graphics.SpriteSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class SpriteJson {

    public static class JsonSprite {
        public String name;
        public int x;
        public int y;
        public FlipEnum flipped;
        public double pixelScale;
        public int width;
        public int height;

        public JsonSprite(int x, int y, String name, FlipEnum flipped, double pixelScale, int height, int width) {
            this.x = x;
            this.y = y;
            this.name = name;
            this.pixelScale = pixelScale;
            this.height = height;
            this.width = width;
            this.flipped = flipped;
        }
    }

    public String spriteSheetPath;

    public int tileSize;
    public double pixelScale;

    public List<JsonSprite> sprites = new ArrayList<>();
    public List<Triplet<String, List<JsonSprite>, Boolean>> spriteSets = new ArrayList<>();

    public static SpriteJson createSpriteJson(JSONObject obj) throws JSONException, ClassCastException {
        SpriteJson json = new SpriteJson();
        json.spriteSheetPath = obj.getString("spriteSheetPath");
        json.tileSize = obj.getInt("tileSize");
        if (obj.has("pixelScale"))
            json.pixelScale = obj.getDouble("pixelScale");
        else json.pixelScale = 4;
        Iterator<String> it = obj.getJSONObject("sprites").keys();
        while (it.hasNext()) {
            String key = it.next();
            JSONObject o = obj.getJSONObject("sprites").getJSONObject(key);
            if (key.equals("Basic_ground") && o.has("tiled")) SpriteManager.tileBorders = o.getBoolean("tiled");
            if (o.has("sprites")) {
                boolean randomized = false;
                boolean ordered = true;
                if (o.has("randomized")) {
                    randomized = o.getBoolean("randomized");
                    ordered = !randomized;
                }
                if (o.has("ordered")) {
                    ordered = o.getBoolean("ordered");
                    randomized = !ordered;
                }
                List<JsonSprite> sprites = new ArrayList<>();
                o.getJSONArray("sprites").forEach(sprite -> {
                    JSONObject spriteObj = ((JSONObject) sprite);
                    sprites.add(parseSprite(spriteObj, json.pixelScale, key));
                });
                json.spriteSets.add(Triplet.of(key, sprites, randomized));
            } else {
                json.sprites.add(parseSprite(o, json.pixelScale, key));
            }
        }
        return json;
    }

    private static JsonSprite parseSprite(JSONObject o, double pixelScale, String key) {
        int x = o.getInt("x");
        int y = o.getInt("y");
        int height;
        int width;
        try {
            height = o.getInt("height");
            width = o.getInt("width");
        } catch (JSONException e) {
            height = width = o.getInt("sideLength");
        }
        FlipEnum flipped = FlipEnum.NONE;
        if (o.has("flipped")) {
            flipped = FlipEnum.valueOf(o.getString("flipped").toUpperCase());
        }
        if (o.has("pixelScale")) {
            pixelScale = o.getDouble("pixelScale");
        }
        return new JsonSprite(x, y, key, flipped, pixelScale, height, width);
    }

}
