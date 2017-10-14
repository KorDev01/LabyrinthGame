package at.fhhgb.mc.helpers.RecursiveBackTrack;

import com.badlogic.gdx.math.Vector2;

/**
 * This class represents a square (Cell) in the labyrinth
 * It has four direction, which can be open or closed,
 * south and east are always opened, if a direction is opened, it
 * means there is a way to the next cell in this direction, else
 * there is a wall
 */
public class Cell {
    // If true, there is a way to the cell in north/south...
    public boolean north, south, east, west;
    public Vector2 position;
    // 'x' = not visited, 'v' visited
    public char val;

    /**
     * The constructor of the class Cell
     * @param _position an Vector2 which describes the position
     *                  of the Cell
     */
    public Cell(Vector2 _position){
        north = false;
        south = false;
        east = false;
        west = false;
        val = 'x';
        position = _position;
    }
}
