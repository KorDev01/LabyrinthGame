package at.fhhgb.mc.gameobjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.Random;

import at.fhhgb.mc.game.MyGame;
import at.fhhgb.mc.helpers.RecursiveBackTrack.Algorithm;
import at.fhhgb.mc.helpers.RecursiveBackTrack.Cell;
import at.fhhgb.mc.screens.GameScreen;

/**
 * This class creates and draws
 * the labyrinth
 */
public class Labyrinth {
    private String TAG = "Lab";
    private int[][] maze;
    private Quadrat[][] quadrats;
    private Block[] borderBlocks;
    private Color blockColor;
    private Vector2 startPt;
    int blockWidth;
    // in Quadrats
    int labW = 10;
    int labH = 10;
    int qSide = 0;

    /**
     * This constructor creates an object of labyrinth
     * @param _myGame Object of MyGame contains all information
     *                necessary for creation
     */
    public Labyrinth(MyGame _myGame){
        labW = _myGame.labW;
        labH = _myGame.labH;
        blockColor = new Color(Color.BLUE);

        int paddingForMenu = Gdx.graphics.getHeight()/9;

        qSide = _myGame.qSide;
        // Width of Walls
        blockWidth = qSide / 5;

        //Point where Lab left top Corner begins
        startPt = new Vector2(0, 0);
        startPt.x = (Gdx.graphics.getWidth()-(labW*qSide))/2;

        if (((Gdx.graphics.getHeight()- (labH * qSide))/2) > 200){
            startPt.y = ((Gdx.graphics.getHeight()- (labH * qSide))/2);
        }else{
            startPt.y = paddingForMenu;
        }

        // Auto generation
        Cell[][] grid = Algorithm.createGrid(labW, labH);
        mazeToQuadrats(_myGame, grid);

        createBorder(_myGame);
    }

    /**
     * This method draws the Labyrinth object
     * @param shapeRenderer is an object of ShapeRenderer, is used
     *                      for drawing rectangles
     * @param batch is an object of SpriteBatch, is used
     *              for drawing images
     */
    public void drawLabyrinth(ShapeRenderer shapeRenderer, Batch batch){
        // +1 For drawing Border
        for (int i = 0; i < labW + 1; i++){
            for (int i1 = 0; i1 < labH + 1; i1++) {
                quadrats[i][i1].drawQuadrat(shapeRenderer ,batch, blockColor);
            }
        }

    }

    /**
     * This method creates the border at the bottom and at right side.
     * @param _myGame object of MyGame
     */
    public void createBorder(MyGame _myGame){
        int x, y, height, length;
        length = qSide * maze.length;
        borderBlocks = new Block[2];

        x = (int)startPt.x + length;
        y = (int)startPt.y;
        height = qSide * labH;
        borderBlocks[0] = new Block(x, y, blockWidth, height, _myGame);
        x = (int)startPt.x + blockWidth;
        y = (int)startPt.y + qSide * labH;
        height = blockWidth;
        borderBlocks[1] = new Block(x, y, length, height, _myGame);
    }

    public Quadrat[][] getQuadrats(){
        return quadrats;
    }

    public Block[] getBorderBlocks(){
        if(borderBlocks != null){
            return borderBlocks;
        }else {
            return null;
        }
    }

    public Block[] getBlocks(){
        LinkedList<Block> blocks = new LinkedList<Block>();
        if(borderBlocks != null){
            for (int i = 0; i < borderBlocks.length; i++){
                blocks.add(borderBlocks[i]);
            }
        }

        for (int i = 0; i < labW; i++){
            for (int i1 = 0; i1 < labH; i1++) {
                if (quadrats[i][i1].getBlocks() != null) {
                    for (int i2 = 0; i2 < quadrats[i][i1].getBlocks().length; i2++) {
                        blocks.add(quadrats[i][i1].getBlocks()[i2]);
                    }
                }
            }
        }
        Block[] blocksArr = new Block[blocks.size()];
        for (int i = 0; i < blocks.size(); i++){
            blocksArr[i] = blocks.get(i);
        }

        return blocksArr;
    }

    public Vector2 getBallStartPos(){
        Vector2 result = new Vector2();
        result.x = startPt.x + blockWidth + 5;
        result.y = startPt.y + blockWidth + 5;
        return result;
    }


    /**
     * Transforms an array of Cells to
     * an array of Integer
     * @param _grid two dimensional array of Cell
     * @return two dimensional array of Int with values 0, 1, 2 or 3
     */
    private int[][] gridToMaze(Cell[][] _grid){
        int[][] result = new int[labW][labH];
        for(int x = 0; x < labW; x++){
            for(int y = 0; y < labH; y++){
                if(_grid[x][y].west && _grid[x][y].north){
                    result[x][y] = 0;
                }else{
                    if(_grid[x][y].west){
                        result[x][y] = 2;
                    }else{
                        if(_grid[x][y].north){
                            result[x][y] = 1;
                        }else{
                            result[x][y] = 3;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * This method transforms the created labyrinth by the class
     * Algorithm to an two dimensional array of Quadrats
     * @param _myGame object of the MyGame class
     * @param _grid an two dimensional array of Cell
     */
    private void mazeToQuadrats(MyGame _myGame, Cell[][] _grid){
        maze = gridToMaze(_grid);
        // Contains Quadrats with info
        quadrats = new Quadrat[labW+1][labH+1];
        //Converting to quadrats
        for (int i = 0; i <= labW; i++){
            for (int i1 = 0; i1 <= labH; i1++){
                Vector2 position = new Vector2();
                position.x = i * qSide + startPt.x;
                position.y = i1 * qSide + startPt.y;
                //Creating Border
                if (i ==  labW && i1 < labH){
                    quadrats[i][i1] = new Quadrat(1, position, blockWidth, _myGame);
                }else if (i1 == labH && i < labW){
                    quadrats[i][i1] = new Quadrat(2, position, blockWidth, _myGame);
                }else if (i < labW && i1 < labH){
                    quadrats[i][i1] = new Quadrat(maze[i][i1], position, blockWidth, _myGame);
                }else{
                    quadrats[i][i1] = new Quadrat(0,position, blockWidth, _myGame);
                }
            }
        }
    }

    public int getBallRad(){
        int result = 0;
        result = qSide/2;
        return result;
    }

    public Vector2 getStartPt(){
        return startPt;
    }
}
