package at.fhhgb.mc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import at.fhhgb.mc.game.MyGame;

/**
 * Created by skorh on 05.12.2016.
 */
public class MenuScreen implements Screen {
    private static final String TAG = "Menu";
    private OrthographicCamera cam;
    private SpriteBatch batch;
    private Stage menuStage;
    private ShapeRenderer shapeRenderer;
    MyGame myGame;

    private float labW;
    private float labH;

    Slider sizeWSlider, sizeHSlider;


    public MenuScreen(MyGame _myGame){
        myGame = _myGame;

        myGame.sec = 0;

        labW = myGame.labW;
        labH = myGame.labH;

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        cam = new OrthographicCamera();
        cam.setToOrtho(true);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);


        menuStage = new Stage();

        //Strings
        String textWelcome = "Menu";
        String textNewGame = "New Game";
        String textExitGame = "Exit";



        //LabelStyle
        Label.LabelStyle style = new Label.LabelStyle(myGame.getSkin().getFont("uiFont"), Color.BLUE);

        //Labels
        Label welcomeLabel = new Label(textWelcome, style);
        float fontScale = Gdx.graphics.getHeight() / 6 / welcomeLabel.getHeight() - 0.2f;
        welcomeLabel.setFontScale(fontScale * 1.5f);


        Color colorNewGameLabel = new Color(74/255f, 142/255f, 249/255f, 1);
        Label newGameLabel = new Label(textNewGame, new Label.LabelStyle(myGame.getSkin().getFont("uiFont"), colorNewGameLabel));
        newGameLabel.setFontScale(fontScale);
        newGameLabel.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                myGame.startGame();
                return true;
            }
        });


        Label exitGameLabel = new Label(textExitGame, new Label.LabelStyle(myGame.getSkin().getFont("uiFont"), colorNewGameLabel));
        exitGameLabel.setFontScale(fontScale);
        exitGameLabel.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                Gdx.app.exit();
                return true;
            }
        });

        //Creating table
        //First Row
        Table table = new Table();
        table.setFillParent(true);
        table.add(welcomeLabel).colspan(2).expandY().padBottom(Gdx.graphics.getHeight()/8);

        //Second Row
        table.row();
        table.add(newGameLabel).colspan(2);
        //Third Row
        table.row();
        table.add(exitGameLabel).colspan(2).padBottom(Gdx.graphics.getHeight() / 4);

        //Adding table to Stage
        menuStage.addActor(table);
        Gdx.input.setInputProcessor(menuStage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Painting Background
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        menuStage.act();

        batch.begin();
        drawLines();
        menuStage.draw();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Gdx.app.log("MenuScreen", "dispose");
        Gdx.input.setInputProcessor(null);
    }

    private void drawLines(){
        int qSide = myGame.calculateQSide(10,10)/2;
        Color lineColor = new Color(60/255f, 134/255f, 252/255f, 1);
        int lineWidth = 2;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(int y = 0 + qSide; y < Gdx.graphics.getHeight(); y += qSide){
            shapeRenderer.setColor(lineColor);
            shapeRenderer.rectLine(0,y,Gdx.graphics.getWidth(),y, lineWidth);
        }
        for(int x = 0 + qSide; x < Gdx.graphics.getWidth(); x+= qSide){
            shapeRenderer.setColor(lineColor);
            shapeRenderer.rectLine(x,0,x,Gdx.graphics.getHeight(), lineWidth);
        }
        Gdx.app.log("MScreen", "drawLine Done");
        shapeRenderer.end();
    }
}
