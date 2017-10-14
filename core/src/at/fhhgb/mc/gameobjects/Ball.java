package at.fhhgb.mc.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import at.fhhgb.mc.game.MyGame;

/**
 * This class represents the ball in the game
 */
public class Ball {

    private Vector2 position;
    private Vector2 velocity;
    private int diameter;
    private Block[] blocks;
    private MyGame myGame;
    private Labyrinth labyrinth;
    private String TAG = "Ball";


    /**
     * Constructor for the class Ball, it initializes all
     * necessary parameters to create an object
     * @param _labyrinth this should be the current Labyrinth object, it
     *                   provides necessary information to create a Ball object
     * @param _myGame should be an object of the current MyGame object, it provides
     *                necessary information to create a Ball object
     */
    public Ball(Labyrinth _labyrinth, MyGame _myGame) {
        super();
        labyrinth = _labyrinth;
        this.diameter = _labyrinth.getBallRad();
        position = new Vector2(_labyrinth.getBallStartPos().x + getRadius(), _labyrinth.getBallStartPos().y + getRadius());
        velocity = new Vector2(0, 0);
        myGame = _myGame;
    }

    /**
     * This method draws the Ball object
     * @param shapeRenderer is an object of ShapeRenderer, which is necessary to render
     *                      the ball
     */
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(position.x, position.y, getRadius());
        shapeRenderer.end();

    }

    /**
     * This method updates the position and velocity of the ball
     * @param delta is a float value which represents the time
     *              passed between last and current frame
     * @param _blocks is an array of Block objects, which are used to
     *                detected collision between ball and blocks.
     */
    public void update(float delta, Block[] _blocks) {
        this.blocks = _blocks;
        updatePosition(delta);
        updateVelocity(delta);

    }

    /**
     * This method detects collision between the Ball object and a Block object
     * @param block an object of Block which collision is checked with ball
     * @return true if there was a collision, else false
     */
    public boolean checkCollision(Block block) {
        if (Intersector.overlaps(getCircle(), block.getRect())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * This method updates the velocity of the Ball object
     * and sets a maximum velocity depending on the fps.
     * The velocity is calculated of values got from the accelerometer
     * @param delta is a float value, which represents the time passed
     *              between two frames
     * @return true if the device has an accelerometer, else false
     */
    public boolean updateVelocity(float delta) {
        int vMax = getVMax((int)labyrinth.blockWidth, delta);
        boolean available = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        if (available) {
            float accelX = Gdx.input.getAccelerometerX();
            float accelY = Gdx.input.getAccelerometerY();
            velocity.x = accelY * 200;
            velocity.y = accelX * 200;

            if (velocity.x > vMax) {
                velocity.x = vMax;
            }
            if (velocity.y > vMax) {
                velocity.y = vMax;
            }
            int vMaxMinus = vMax * -1;
            if (velocity.x < vMaxMinus) {
                velocity.x = vMaxMinus;
            }
            if (velocity.y < vMaxMinus) {
                velocity.y = vMaxMinus;
            }
        }
        return available;
    }

    /**
     * This method updates the position of the ball, it uses the values set
     * by the updateVelocity function.
     * This method is also responsible for collision detection and handling
     * @param delta is a float value, which represents the time passed
     *              between two frames
     */
    public void updatePosition(float delta) {
        int ballX = (int) (getX());
        int ballY = (int) (getY());

        position.add(velocity.cpy().scl(delta));
        // Collision with Points
        for (int i = 0; i < myGame.points.length; i++){
            if (myGame.points[i] != null && Intersector.overlaps(getCircle(), myGame.points[i])){
                int plusSeconds = myGame.labW/myGame.points.length;
                myGame.sec += plusSeconds;
                myGame.pointsCollected++;
                Gdx.app.log(TAG, "Collision with Point: " + i);
                myGame.points[i] = null;
                myGame.gameScreen.gameRender.showPlusTimeLabel(ballX, ballY, plusSeconds);
            }
        }

        for (int i = 0; i < blocks.length; i++) {
            Block block = blocks[i];
            if (checkCollision(block)) {
                // Ball under Block
                boolean processed = false;
                if (ballY >= block.getRect().getY() + block.getRect().getHeight() && (ballX >= (block.getRect().getX())
                        && ballX <= (block.getRect().getX() + block.getRect().getWidth()))) {
                    position.y = block.getRect().getY() + block.getRect().getHeight() + getRadius();
                    velocity.y = 0;
                    processed = true;
                    //Gdx.app.log(TAG, "underBlock");
                }

                // Ball above Block
                if (ballY <= block.getRect().getY() && (ballX >= (block.getRect().getX())
                        && ballX <= (block.getRect().getX() + block.getRect().getWidth()))) {
                    position.y = block.getRect().getY() - getRadius();
                    velocity.y = 0;
                    processed = true;
                    //Gdx.app.log(TAG, "aboveBlock");
                }

                // Ball left from Block
                if (ballX <= block.getRect().getX() && (ballY >= block.getRect().getY()
                        && ballY <= block.getRect().getY() + block.getRect().getHeight())) {
                    position.x = block.getRect().getX() - getRadius();
                    velocity.x = 0;
                    processed = true;
                    //Gdx.app.log(TAG, "leftBlock");
                }

                // Ball right from Block
                if (ballX >= block.getRect().getX() + block.getRect().getWidth() && (ballY >= block.getRect().getY()
                        && ballY <= block.getRect().getY() + block.getRect().getHeight())) {
                    position.x = block.getRect().getX() + block.getRect().getWidth() + getRadius();
                    velocity.x = 0;
                    processed = true;
                    //Gdx.app.log(TAG, "rightBlock");
                }
            }
        }

        if (position.x > Gdx.graphics.getWidth()) {
            position.x = 0;
        }
        if (position.x < 0) {
            position.x = Gdx.graphics.getWidth();
        }
        if (position.y > Gdx.graphics.getHeight()) {
            position.y = 0;
        }
        if (position.y < 0) {
            position.y = Gdx.graphics.getHeight();
        }
    }

    private int getVMax(int _blockWidth, float delta) {
        double result = 0;
        double fps = 1 / delta;
        result = _blockWidth * fps;
        return (int) result;
    }

    public Circle getCircle() {
        return new Circle(getX(), getY(), getRadius());
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getRadius() {
        return diameter / 2;
    }


}
