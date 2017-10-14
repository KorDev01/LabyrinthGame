package at.fhhgb.mc.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import at.fhhgb.mc.game.MyGame;

/**
 * This class represents a square,
 * which can contain 0, 1 or two walls
 */
public class Quadrat {
    private Block[] blocks;
    private int type;

    /**
     * Constructor of the class Quadrat
     * @param type it is an int, which describes how the walls are placed
     *             in the Quadrat
     * @param position an object of Vector2, which describes where the
     *                 Quadrat is placed, in pixels
     * @param width an Integer with defines the length of a Quadarat
     * @param _myGame is an object of MyGame
     */
    public Quadrat(int type, Vector2 position, int width, MyGame _myGame){
        blocks = null;
        this.type = type;
        if (type == 1){
            blocks = new Block[1];
            blocks[0] = new Block((int)position.x, (int)position.y, width + 1, 5 * width, _myGame);
        }
        if (type == 2){
            blocks = new Block[1];
            blocks[0] = new Block((int)position.x, (int)position.y,  5 * width, width, _myGame);
        }
        if (type == 3){
            blocks = new Block[2];
            blocks[0] = new Block((int)position.x, (int)position.y, width, 5 * width, _myGame);
            blocks[1] = new Block((int)position.x + width, (int)position.y, 4 * width, width, _myGame);
        }
    }

    /**
     * This method draws the Quadrat
     * @param shapeRenderer is an object of ShapeRenderer, is used
     *                      for drawing rectangles
     * @param batch is an object of SpriteBatch, is used
     *              for drawing images
     * @param color the color of Rectangles, only for testing
     */
    public void drawQuadrat(ShapeRenderer shapeRenderer, Batch batch, Color color){
        if (blocks != null) {
                blocks[0].draw(blocks[0].getRect(), batch, type);
        }
    }

    public Block[] getBlocks() {
        return blocks;
    }
}
