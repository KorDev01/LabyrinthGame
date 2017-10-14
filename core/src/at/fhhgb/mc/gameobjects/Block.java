package at.fhhgb.mc.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import at.fhhgb.mc.game.MyGame;

/**
 * This class is used to represent the
 * walls in the game
 */
public class Block{

    private Rectangle block;
    private Texture image1, image2, image3;
    private final String TAG = "Block";

    /**
     * The constructor of the class Block
     * @param x x position where the block should be placed
     * @param y y position where the block should be placed
     * @param width the width of the block
     * @param height the height of the block
     * @param _myGame an object of MyGame, which contains all
     *                important variables
     */
    public Block(int x, int y, int width, int height, MyGame _myGame){
        super();
        block = new Rectangle(x, y, width, height);
        image1 = _myGame.assetsLoader.image1;
        image2 = _myGame.assetsLoader.image2;
        image3 = _myGame.assetsLoader.image3;
    }

    /**
     * This method draws the walls, using images
     * @param rect this Rectangle object is used to get the dimensions
     * @param batch is a SpriteBatch object to draw the walls
     * @param type is a type of the wall, used for choosing the right image
     */
    public void draw(Rectangle rect, Batch batch, int type){
        int x = (int)rect.getX();
        int y = (int)rect.getY();
        int width = (int)rect.width;
        int height = (int)rect.height;
        if (type == 1){
            batch.draw(image1, x,y, width, height);
            Gdx.app.log(TAG, "type = 1");
        }
        if (type == 2){
            batch.draw(image2, x,y, width, height);
            Gdx.app.log(TAG, "type = 2");
        }
        if (type == 3){
            width = height;
            batch.draw(image3, x,y, width, height, 0, 0, image3.getWidth(), image3.getHeight(),false, true);
            Gdx.app.log(TAG, "type = 3");
        }
    }

    /**
     * This method draws blocks as rectangles,
     * was used for testing
     * @param rect this Rectangle object is used to get the dimensions
     * @param shapeRenderer is a ShapeRenderer object to draw the walls
     * @param color in which color the block will be painted
     */
    public void drawBlock(Rectangle rect, ShapeRenderer shapeRenderer, Color color){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        int x = (int)rect.getX();
        int y = (int)rect.getY();
        int width = (int)rect.width;
        int height = (int)rect.height;
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }

    public Rectangle getRect(){
        return block;
    }

}
