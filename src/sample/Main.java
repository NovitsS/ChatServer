package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.NetWork.NetServer;
import sample.util.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
    private NetServer netServer;
    public static HashMap<String, User> map=null;
    public static HashMap<String,ArrayList<String>> msgMap=null;
    @Override

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("resources/sample.fxml"));
        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        map = new HashMap<String,User>();
        msgMap = new HashMap<String,ArrayList<String>>();
        netServer = NetServer.getInstance();
        netServer.startLearning();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void initServerData() {
        //TODO:加载服务器上的用户信息
    }
}
