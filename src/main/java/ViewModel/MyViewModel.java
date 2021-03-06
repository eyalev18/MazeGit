package ViewModel;

import Model.IModel;
import Model.MovementDirection;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this); //Observe the Model for it's changes
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public Maze getMaze(){
        return model.getMaze();
    }

    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    public int getPlayerCol(){
        return model.getPlayerCol();
    }

    public Solution getSolution(){
        return model.getSolution();
    }

    public void generateMaze(int rows, int cols){
        model.generateMaze(rows, cols);
    }

    public void movePlayer(KeyEvent keyEvent){
        MovementDirection direction;
        switch (keyEvent.getCode()){
            case NUMPAD9:
                direction = MovementDirection.NUMPAD9;
                break;
            case NUMPAD8:
                direction = MovementDirection.NUMPAD8;
                break;
            case NUMPAD7:
                direction = MovementDirection.NUMPAD7;
                break;
            case NUMPAD6:
                direction = MovementDirection.NUMPAD6;
                break;
            case NUMPAD4:
                direction = MovementDirection.NUMPAD4;
                break;
            case NUMPAD3:
                direction = MovementDirection.NUMPAD3;
                break;
            case NUMPAD2:
                direction = MovementDirection.NUMPAD2;
                break;
            case NUMPAD1:
                direction = MovementDirection.NUMPAD1;
                break;
            case UP:
                direction = MovementDirection.UP;
                break;
            case DOWN:
                direction = MovementDirection.DOWN;
                break;
            case LEFT:
                direction = MovementDirection.LEFT;
                break;
            case RIGHT:
                direction = MovementDirection.RIGHT;
                break;
            default:
                // no need to move the player
                return;
        }
        model.updatePlayerLocation(direction);
    }

    public void solveMaze(){
        model.solveMaze();
    }

    public void propertiesDisplay() {
        model.propertiesDisplay();
    }

    public void music() {
        model.music();
    }

    public void saveMaze(String chosen) throws IOException {
        model.saveMaze(chosen);
    }

    public void open(File chosen) throws IOException {
        model.open(chosen);
    }

    public void Unmute() {
        model.Unmute();
    }

    public void Mute() {
        model.Mute();
    }

    public void movePlayerByDrag(String move) {
        MovementDirection direction;
        if (getMaze() != null) {
            switch (move) {
                case "UP":
                    direction = MovementDirection.UP;
                    break;
                case "DOWN":
                    direction = MovementDirection.DOWN;
                    break;
                case "LEFT":
                    direction = MovementDirection.LEFT;
                    break;
                case "RIGHT":
                    direction = MovementDirection.RIGHT;
                    break;
                default:
                    // no need to move the player
                    return;
            }
            model.updatePlayerLocation(direction);
        }
    }

    public void setProperties(String thread, String gen, String search) {
        model.setProperties(thread, gen, search);
    }
}