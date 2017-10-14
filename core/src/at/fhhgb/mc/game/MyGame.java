package at.fhhgb.mc.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import at.fhhgb.mc.helpers.AssetsLoader;
import at.fhhgb.mc.screens.GameScreen;
import at.fhhgb.mc.screens.MenuScreen;

/**
 * This class is the root class of the
 * game.
 */
public class MyGame extends Game {
	private Skin uiSkin;
	public int labW = 10;
	public int labH = 10;
	public int qSide = 0;
	public AssetsLoader assetsLoader;
	public Circle[] points;
	public int sec;
	public int pointsCollected;
	public int maxPointsAmount;
	public GameScreen gameScreen;

	/**
	 * This method creates an object of MyGame,
	 * and initialises all necessary variables
	 */
	@Override
	public void create() {
		createSkin();
		generateWidthHeight();
		qSide = calculateQSide(labW, labH);
		setScreen(new MenuScreen(this));
		//Should always be even
		maxPointsAmount = 6;
		sec = 15;
		assetsLoader = new AssetsLoader(this);
	}

	/**
	 * This method creates an new object of GameScreen,
	 * this object is saved in the variable gameScreen.
	 * It shows the gameScreen.
	 * It calculates the length of a Quadrat (Wall length)
	 * and saves it to the variable qSide
	 */
	public void startGame(){
		qSide = calculateQSide(labW, labH);
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}

	/**
	 * This method creates an object of MenuScreen
	 * and shows the menu
	 */
	public void startMenu(){
		setScreen(new MenuScreen(this));
	};

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * This method creates a skin object, which
	 * is saved in the variable uiSkin, this object
	 * contains all the necessary information for creating a Label, Button or other
	 * UI object.
	 */
	private void createSkin(){
		//Init resources
		// Init font
		uiSkin = new Skin();
		BitmapFont uiFont = new BitmapFont(Gdx.files.internal("myFont.fnt"));
		uiFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		uiSkin.add("uiFont", uiFont);
		//Init btn pic
		Texture okTexture = new Texture(Gdx.files.internal("accept.png"));
		uiSkin.add("okBtnImg",okTexture);

		Texture cancelTexture = new Texture(Gdx.files.internal("cancel.png"));
		uiSkin.add("cancelBtnImg", cancelTexture);

		//SliderTextures
		uiSkin.add("sliderBack", new Texture(Gdx.files.internal("slider-after.png")));
		uiSkin.add("sliderKnob", new Texture(Gdx.files.internal("slider-knob.png")));
		uiSkin.add("sliderAfter", new Texture(Gdx.files.internal("slider-after.png")));
		uiSkin.add("sliderBefore", new Texture(Gdx.files.internal("slider-before.png")));
	}

	public Skin getSkin(){return uiSkin;}

	/**
	 * This method calculates the side length of square,
	 * which is also the length of a wall in pixel
	 * @param labW the width of the labyrinth in Quadrats
	 * @param labH the height of the labyrinth in Quadrats
     * @return an integer, which is the side of a Quadrat in pixels
     */
	public int calculateQSide(int labW, int labH){
		//Setting Lab to middle of Screen
		int qSide = 0;
		int paddingForMenu = Gdx.graphics.getHeight()/9;
		if (( (Gdx.graphics.getWidth() - (Gdx.graphics.getWidth()/20)) / labW ) < ( ((Gdx.graphics.getHeight() - paddingForMenu) - (Gdx.graphics.getHeight()/20)) / labH )){
			qSide = (Gdx.graphics.getWidth() - (Gdx.graphics.getWidth()/20)) / labW;
		}else{
			qSide = ((Gdx.graphics.getHeight()- paddingForMenu) - (Gdx.graphics.getHeight()/20)) / labH;
		}
		// Deleting floating points and rounding
		qSide = qSide / 5;
		qSide = qSide * 5;
		return qSide;
	}

	/**
	 * This method generates the width and height
	 * of the labyrinth in Quadrats, depending on the
	 * device screen resolution. The height in set
	 * to ten Quadrats.
	 */
	private void generateWidthHeight(){
		double w = Gdx.graphics.getWidth();
		double h = Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 9.0;
		double prop =  w / h;
		labH = 10;
		labW = (int) (labH * prop);
	}

}
