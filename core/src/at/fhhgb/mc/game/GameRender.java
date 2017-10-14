package at.fhhgb.mc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;

import java.util.Random;

import at.fhhgb.mc.gameobjects.Ball;
import at.fhhgb.mc.gameobjects.Labyrinth;

/**
 * This class manages the game process
 */
public class GameRender {
    private static final String TAG = "Render";

    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private MyGame myGame;
    private Stage gameStage;
    private Vector2 startPt;
    private Ball ball;
    private Labyrinth labyrinth;
    private ParticleEffect[] pointEff;
    private Label timeLabel;
    private Label gameOverLabel;
    private Label newGameLabel;
    private Timer timer;
    private boolean gameOver;

    /**
     * Creates an object of GameRender
     * and initialises all variables and objects
     * which are necessary to create and play the
     * game.
     * @param _myGame is an object of the MyGame class, which contains important
     *                variables for communicating between objects of different
     *                classes
     */
    public GameRender(MyGame _myGame) {
        myGame = _myGame;
        cam = new OrthographicCamera();
        cam.setToOrtho(true);
        gameStage = new Stage();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        createLabyrinth();
        createBall();
        createUI();

        startPt = new Vector2(labyrinth.getStartPt());
        startPt.x = startPt.x - myGame.labW * myGame.qSide / 2;
        startPt.y = startPt.y - myGame.labH * myGame.qSide / 2;

        startGame();
    }

    /**
     * This method updates all objects in the game, such as ball and walls,
     * all interaction is done in this method
     * @param delta the time between two frames
     */
    public void update(float delta) {
        for (int i = 0; i < pointEff.length; i++) {
            if (pointEff[i] != null) {
                pointEff[i].update(delta);
            }
        }
        for (int i = 0; i < pointEff.length; i++){
            if(myGame.points[i] == null){
                pointEff[i] = null;
            }
        }
        if (myGame.pointsCollected == pointEff.length && !gameOver){
            startGame();
        }
        ball.update(delta, labyrinth.getBlocks());
    }

    /**
     * This method draws all objects on the screen
     * @param runTime how much time left after the start of the game
     */
    public void render(float runTime) {
        gameStage.act();
        Gdx.gl.glClearColor(250 / 255f, 250 / 255f, 250 / 255f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Drawing lines
        drawLines();

        batch.begin();
        //Drawing EndPointEffekt
        for (int i = 0; i < pointEff.length; i++) {
            if (pointEff[i] != null) {
                pointEff[i].draw(batch);
            }
        }
        // Here painting other objects
        labyrinth.drawLabyrinth(shapeRenderer, batch);
        // Drawing Ball

        batch.end();
        //Draw Ball
        ball.draw(shapeRenderer);
        //Draw dark BG when GameOver
        if(gameOver){
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.6f));
            shapeRenderer.rect(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        //Draw UI
        gameStage.draw();
    }

    /**
     * This method creates particle effects,
     * which represent the points, which are
     * saved in the MyGame object in the array points.
     */
    private void createPointEffects() {
        Random random = new Random();
        int points = random.nextInt(myGame.maxPointsAmount/2+1) + myGame.maxPointsAmount/2;
        Gdx.app.log(TAG, "Amount of points: " + points);
        myGame.points = null;
        myGame.points = new Circle[points];
        pointEff = null;
        pointEff = new ParticleEffect[points];
        int[] rWArr = new int[pointEff.length];
        for (int i = 0; i < pointEff.length; i++) {
            int rW = random.nextInt(myGame.labW);
            for (int j = 0; j < rWArr.length; j++) {
                if (rW == rWArr[j]) {
                    rW = random.nextInt(myGame.labW);
                    j = 0;
                }
            }
            rWArr[i] = rW;
            pointEff[i] = createPointEff(i, rW);
            pointEff[i].start();
        }
    }

    /**
     * This method creates one point for the method
     * createPointEffects
     * @param _imgN the number which file should be choosen
     *              for this point
     * @param _rW is the number of the row in which the point should
     *            be placed
     * @return a particle effect
     */
    private ParticleEffect createPointEff(int _imgN, int _rW) {
        ParticleEffect point;
        Random random = new Random();
        point = new ParticleEffect(myGame.assetsLoader.pointEffect[_imgN]);
        // random pos
        int rW = _rW;
        float x = labyrinth.getStartPt().x + rW * myGame.qSide + myGame.qSide / 5 + ball.getRadius() + ball.getRadius() / 3;
        int rH = random.nextInt(myGame.labH);
        float y = labyrinth.getStartPt().y + rH * myGame.qSide + myGame.qSide / 5 + ball.getRadius() + ball.getRadius() / 3;

        point.getEmitters().first().setPosition(x, y);
        //Creating Circles to detect collision with points
        myGame.points[_imgN] = new Circle(x, y, ball.getRadius());
        point.scaleEffect(ball.getRadius() * 2 / 34);
        return point;
    }

    /**
     * This method creates the ball
     */
    private void createBall() {
        ball = new Ball(labyrinth, myGame);
    }

    /**
     * This method creates the labyrinth
     */
    private void createLabyrinth() {
        labyrinth = null;
        labyrinth = new Labyrinth(myGame);
    }

    /**
     * This method creates the user interface
     */
    private void createUI() {
        String textMenu = "Menu";
        String time = "Time: 666";
        String gameOver = "gameOver";
        String newGame = "NEW LABYRINTH";
        //LabelStyle
        Label.LabelStyle style = new Label.LabelStyle(myGame.getSkin().getFont("uiFont"), Color.BLUE);
        //Labels
        Label menuLabel = new Label(textMenu, style);
        float fontScale = Gdx.graphics.getHeight() / 9 / menuLabel.getHeight() - 0.2f;
        menuLabel.setFontScale(fontScale);
        menuLabel.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                myGame.startMenu();
                return true;
            }
        });

        timeLabel = new Label(time, style);
        timeLabel.setFontScale(fontScale);

        gameOverLabel = new Label(gameOver, style);

        float x = 0;
        float y = 0;
        x = Gdx.graphics.getWidth()/2 - gameOverLabel.getWidth() * fontScale;
        y = Gdx.graphics.getHeight()/2 + gameOverLabel.getHeight();

        gameOverLabel.setStyle(new Label.LabelStyle(myGame.getSkin().getFont("uiFont"), new Color(1, 0, 0, 0)));
        gameOverLabel.setFontScale(fontScale * 2);
        gameOverLabel.setPosition(x, y);


        newGameLabel = new Label(newGame, new Label.LabelStyle(myGame.getSkin().getFont("uiFont"), Color.GREEN));
        newGameLabel.setFontScale(fontScale);
        x = (Gdx.graphics.getWidth()/2) - (newGameLabel.getWidth()*fontScale)/2;
        y = Gdx.graphics.getHeight()/2 - (newGameLabel.getHeight()*fontScale)/2;
        newGameLabel.setPosition(x, y);

        //Table
        Table tableMenu = new Table();
        tableMenu.setFillParent(true);
        tableMenu.add(menuLabel).left().top().expand();

        Table tableTime = new Table();
        tableTime.setFillParent(true);
        tableTime.add(timeLabel).right().top().expand();
        gameStage.addActor(tableMenu);
        gameStage.addActor(tableTime);

        Gdx.input.setInputProcessor(gameStage);
    }

    /**
     * This method draw the lines
     * which simulates a quad paper
     */
    private void drawLines() {
        int qSide = myGame.qSide / 2;
        Color lineColor = new Color(60 / 255f, 134 / 255f, 252 / 255f, 1);
        int lineWidth = 2;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int y = (int) startPt.y + qSide; y < Gdx.graphics.getHeight(); y += qSide) {
            shapeRenderer.setColor(lineColor);
            shapeRenderer.rectLine(0, y, Gdx.graphics.getWidth(), y, lineWidth);
        }
        for (int x = (int) startPt.x + qSide; x < Gdx.graphics.getWidth(); x += qSide) {
            shapeRenderer.setColor(lineColor);
            shapeRenderer.rectLine(x, 0, x, Gdx.graphics.getHeight(), lineWidth);
        }
        shapeRenderer.end();
    }

    /**
     * This method disposes all points
     */
    public void dispose() {
        for (int i = 0; i < pointEff.length; i++) {
            pointEff[i].dispose();
        }
    }

    /**
     * This method creates and starts the
     * timer which counts down the time
     * and shows it in the label on the top right
     * side of the screen.
     */
    public void startTimer() {
        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (GameRender.this.myGame.sec <= 0){
                    gameOver();
                }
                int sec = GameRender.this.myGame.sec--;
                String seconds = "";
                if (sec < 10) {
                    seconds = "00" + sec;
                } else if (sec < 100) {
                    seconds = "0" + sec;
                } else {
                    seconds = "" + sec;
                }
                timeLabel.setText("Time: " + seconds);
            }
        }, 0, 1);
    }

    /**
     * This method destroys the timer
     * which counts the time down
     */
    public void killTimer(){
        if(timer != null){
            timer.clear();
        }
    }

    /**
     * This method does all necessary things,
     * for finishing the game, such as showing the
     * game over label, and disabling the game
     */
    public void gameOver(){
        killTimer();
        gameOver = true;
        gameStage.addActor(gameOverLabel);
        myGame.sec = 0;

        final Timer timerGameOver = new Timer();
        timerGameOver.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                float alpha = gameOverLabel.getStyle().fontColor.a + 0.02f;
                Color color = new Color(255/255f, 0f, 0f, alpha);
                gameOverLabel.setStyle(new Label.LabelStyle(myGame.getSkin().getFont("uiFont"), color));
                if(alpha >= 1){
                    timerGameOver.clear();
                }
            }
        }, 0, 0.04f);
    }

    /**
     * This method shows a label which shows how much time will
     * be added to the time.
     * This label is shown when a point is collected
     * @param _x x position of the label
     * @param _y y position of the label
     * @param _plusSeconds how much second will be added
     */
    public void showPlusTimeLabel(int _x, int _y, int _plusSeconds){
        final int x = _x;
        final int y = Gdx.graphics.getHeight() - _y;
        final int plusSeconds = _plusSeconds;

        final Label plusTimeLabel = new Label("+5", new Label.LabelStyle(myGame.getSkin().getFont("uiFont"), Color.GREEN));
        plusTimeLabel.setFontScale(2);
        plusTimeLabel.setText("+" + plusSeconds);
        plusTimeLabel.setPosition(x, y);

        gameStage.addActor(plusTimeLabel);

        final Timer timerShowTimePlusLabel = new Timer();
        timerShowTimePlusLabel.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                float yPos = plusTimeLabel.getY() + 20;
                float alpha = plusTimeLabel.getStyle().fontColor.a - 0.03f;
                Color color = new Color(0f, 255/255f, 0f, alpha);

                plusTimeLabel.setStyle(new Label.LabelStyle(myGame.getSkin().getFont("uiFont"), color));
                plusTimeLabel.setPosition(x, yPos);

                if(yPos > Gdx.graphics.getHeight() || alpha <= 0){
                    plusTimeLabel.remove();
                    timerShowTimePlusLabel.clear();
                }
            }
        }, 0, 0.04f);
    }

    /**
     * This method calculates the start time
     * for a new labyrinth
     */
    private void calculateTime(){
        if(myGame.sec < 10){
            myGame.sec = 10;
        }else{
            myGame.sec += 3;
        }
    }

    /**
     * This method starts the game
     */
    private void startGame(){
        showNewGameLabel();
        createLabyrinth();
        createPointEffects();
        myGame.pointsCollected = 0;
        calculateTime();
        gameOver = false;
    }

    /**
     * This method shows a label when a new labyrinth is created
     */
    private void showNewGameLabel(){
        newGameLabel.setStyle(new Label.LabelStyle(myGame.getSkin().getFont("uiFont"), Color.GREEN));
        gameStage.addActor(newGameLabel);
        final Timer timerShowTimePlusLabel = new Timer();
        timerShowTimePlusLabel.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                float alpha = newGameLabel.getStyle().fontColor.a - 0.03f;
                Color color = new Color(0f, 255/255f, 0f, alpha);
                newGameLabel.setStyle(new Label.LabelStyle(myGame.getSkin().getFont("uiFont"), color));
                if( alpha <= 0){
                    newGameLabel.remove();
                    timerShowTimePlusLabel.clear();
                }
            }
        }, 0, 0.04f);
    }

}
