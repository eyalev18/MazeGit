package View;

import ViewModel.MyViewModel;
import javafx.stage.Stage;

public interface IView {
    void setViewModel(MyViewModel viewModel);
    void setUpdatePlayerRow(int updatePlayerRow);
    void setUpdatePlayerCol(int updatePlayerCol);
    void setPlayerPosition(int row, int col);
    void dragMove();
    void getStage(Stage primaryStage);
}