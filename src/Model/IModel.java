package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.Node;
import javafx.scene.control.Menu;

import java.util.Observer;

public interface IModel {
    void generateMaze(int rows, int cols);
    Maze getMaze();
    void updatePlayerLocation(MovementDirection direction);
    int getPlayerRow();
    int getPlayerCol();
    void assignObserver(Observer o);
    void solveMaze();
    Solution getSolution();
    void propertiesDisplay();
}
