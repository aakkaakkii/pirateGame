package org.example.components;

import java.awt.*;

public class SpriteRenderer extends Component implements Drawer {
    private Sprite sprite;

    public SpriteRenderer() {
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void draw(Graphics2D g2) {
        if(sprite != null) {


            g2.drawImage(sprite.image,
                    (int) (gameObject.transform.position.x ),
                    (int) (gameObject.transform.position.y ),
                    (int) (sprite.width * gameObject.transform.scale.x),
                    (int) (sprite.height * gameObject.transform.scale.y),
                    null);


 /*           g2.drawImage(sprite.image,
                    gameObject.transform.scale
                    (int) (gameObject.transform.position.x - (sprite.width * gameObject.transform.scale.x * 0.5) ),
                    (int) (gameObject.transform.position.y - (sprite.height * gameObject.transform.scale.y * 0.5) ),
                    (int) (sprite.width * gameObject.transform.scale.x),
                    (int) (sprite.height * gameObject.transform.scale.y),
                    null);*/
        }
    }
}
