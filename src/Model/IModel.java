package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.Node;
import javafx.scene.control.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    void music();
    void saveMaze(String chosen) throws IOException;
    void open(File chosen) throws IOException;
}
