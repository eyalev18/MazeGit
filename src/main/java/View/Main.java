package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("The Amazing Maze Project");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.getStage(primaryStage);
        view.setViewModel(viewModel);
    }

    public static void main(String[] args) {
        launch(args);
    }

}