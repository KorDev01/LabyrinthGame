package at.fhhgb.mc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import at.fhhgb.mc.game.MyGame;
import at.fhhgb.mc.game.GameRender;

/**
 * Created by skorh on 21.10.2016.
 */
public class GameScreen implements Screen{
    private String TAG = "GameScreen";
    public GameRender gameRender;
    private float runTime = 0;



    public GameScreen(MyGame _myGame){
        gameRender = new GameRender(_myGame);
        Gdx.app.log(TAG,  "new GameScreen");
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show called");
    }

    @Override
    public void render(float delta) {
        float fps = 1/delta;
        //Gdx.app.log(TAG, "FPS: " + fps);
        runTime += delta;
        gameRender.update(delta);
        gameRender.render(runTime);
    }

    @Override
    public void resize(int width, int height) {
        gameRender.startTimer();
        Gdx.app.log("GameScreen", "resizing");
    }

    @Override
    public void pause() {
        gameRender.killTimer();
        Gdx.app.log("GameScreen", "pause called");
    }

    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume called");
    }

    @Override
    public void hide() {
        gameRender.killTimer();
        Gdx.app.log("GameScreen", "hide called");
    }

    @Override
    public void dispose() {
        Gdx.app.log("GameScreen", "dispose called");
        gameRender.dispose();
    }
}
