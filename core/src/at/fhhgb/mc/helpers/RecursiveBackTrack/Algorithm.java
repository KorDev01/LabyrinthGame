package at.fhhgb.mc.helpers.RecursiveBackTrack;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * This class creates an labyrinth with
 * customized dimension using the backtracking
 * recursive algorithm
 */
public class Algorithm {

    static Cell[][] grid;
    private static int gridW = 16;
    private static int gridH = 9;

    /**
     * This method creates the labyrinth
     * @param width the width of labyrinth in Cell
     * @param height the height of labyrinth in Cell
     * @return an two dimensional array[width, height] of Cell
     */
    public static Cell[][] createGrid(int width, int height) {
        gridW = width;
        gridH = height;

        grid = new Cell[gridW][gridH];
        // Grid init
        for(int x = 0; x < gridW; x++){
            for (int y = 0; y < gridH; y++){
                grid[x][y] = new Cell(new Vector2(x,y));
                //System.out.println(grid[x][y]);
            }
        }
        int c = 0;
        c = createLab(new Vector2(0,0), 0);
        System.out.println(c);
        return grid;
    }

    private static int createLab(Vector2 _curPosition, int counter){
        if(grid[(int)_curPosition.x][(int)_curPosition.y].val == 'x'){

            grid[(int)_curPosition.x][(int)_curPosition.y].val = 'v';
            Random rand = new Random();
            int order = rand.nextInt(4);

            switch(order){
                case 0:
                    counter += goWest(_curPosition, counter);
                    counter += goNorth(_curPosition, counter);
                    counter += goEast(_curPosition, counter);
                    counter += goSouth(_curPosition, counter);
                    break;
                case 1:
                    counter += goSouth(_curPosition, counter);
                    counter += goNorth(_curPosition, counter);
                    counter += goEast(_curPosition, counter);
                    counter += goNorth(_curPosition, counter);
                    break;
                case 2:
                    counter += goNorth(_curPosition, counter);
                    counter += goSouth(_curPosition, counter);
                    counter += goEast(_curPosition, counter);
                    counter += goWest(_curPosition, counter);
                    break;
                case 3:
                    counter += goEast(_curPosition, counter);
                    counter += goWest(_curPosition, counter);
                    counter += goSouth(_curPosition, counter);
                    counter += goNorth(_curPosition, counter);
                    break;
            }
        }
        return ++counter;
    }

    private static int goWest(Vector2 _curPosition, int counter){
        if (_curPosition.x - 1 >= 0){
            if(grid[(int)_curPosition.x - 1][(int)_curPosition.y].val == 'x'){
                // west
                //System.out.println("Went west");
                grid[(int)_curPosition.x][(int)_curPosition.y].west = true;
                grid[(int)_curPosition.x - 1][(int)_curPosition.y].east = true;
                counter += createLab(new Vector2(_curPosition.x - 1, _curPosition.y), counter);
            }
        }
        return counter;
    }

    private static int goNorth(Vector2 _curPosition, int counter){
        if (_curPosition.y - 1 >= 0){
            if(grid[(int)_curPosition.x][(int)_curPosition.y - 1].val == 'x'){
                // north
                //System.out.println("Went north");
                grid[(int)_curPosition.x][(int)_curPosition.y - 1].south = true;
                grid[(int)_curPosition.x][(int)_curPosition.y].north = true;
                counter += createLab(new Vector2(_curPosition.x, _curPosition.y - 1), counter);
            }
        }
        return counter;
    }

    private static int goEast(Vector2 _curPosition, int counter){
        if (_curPosition.x + 1 < gridW){
            if(grid[(int)_curPosition.x + 1][(int)_curPosition.y].val == 'x'){
                // east
                //System.out.println("Went east");
                grid[(int)_curPosition.x + 1][(int)_curPosition.y].west = true;
                grid[(int)_curPosition.x][(int)_curPosition.y].east = true;
                counter += createLab(new Vector2(_curPosition.x + 1, _curPosition.y), counter);
            }
        }
        return counter;
    }

    private static int goSouth(Vector2 _curPosition, int counter){
        if (_curPosition.y + 1 < gridH){
            if(grid[(int)_curPosition.x][(int)_curPosition.y + 1].val == 'x'){
                // south
                //System.out.println("Went south");
                grid[(int)_curPosition.x][(int)_curPosition.y + 1].north = true;
                grid[(int)_curPosition.x][(int)_curPosition.y].south = true;
                counter += createLab(new Vector2(_curPosition.x, _curPosition.y + 1), counter);
            }
        }
        return counter;
    }

}
