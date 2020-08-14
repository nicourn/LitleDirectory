package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import javafx.stage.FileChooser;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

import java.util.ArrayList;

import java.io.*;
import java.util.Scanner;

public class Main extends Application {
    int WIDTH = 200;
    ObservableList<People> tableInfo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
//        Графа ввода "по балам"
        HBox byScores = new HBox();
        Label byScoresLabel = new Label("Кількість балів");
        HBox.setHgrow(byScoresLabel, Priority.ALWAYS);
        byScoresLabel.setMaxWidth(WIDTH * 0.75);

        TextField byScoresText = new TextField();
        HBox.setHgrow(byScoresText, Priority.ALWAYS);
        byScoresText.setMaxWidth(WIDTH);
        byScores.getChildren().addAll(byScoresLabel, byScoresText);

//        Графа ввода "по школе"
        HBox byNumSchool = new HBox();
        Label byNumSchoolLabel = new Label("Школа");
        HBox.setHgrow(byNumSchoolLabel, Priority.ALWAYS);
        byNumSchoolLabel.setMaxWidth(WIDTH * 0.75 );

        TextField byNumSchoolText = new TextField();
        HBox.setHgrow(byNumSchoolText, Priority.ALWAYS);
        byNumSchoolText.setMaxWidth(WIDTH);
        byNumSchool.getChildren().addAll(byNumSchoolLabel, byNumSchoolText);
        //        Поиск 200 бальников
        HBox byHigth = new HBox(10);


        Button searchByHight = new Button("По 200 балів");
        HBox.setHgrow(searchByHight, Priority.ALWAYS);
        searchByHight.setMaxWidth(WIDTH);

        Label byHigthLabel = new Label();
        HBox.setHgrow(byHigthLabel, Priority.ALWAYS);
        byHigthLabel.setMaxWidth(WIDTH);

        byHigth.getChildren().addAll(searchByHight, byHigthLabel);


//        Поиск по параметрам
        HBox searchBox = new HBox(10);

        Button search = new Button("Пошук");
        HBox.setHgrow(search, Priority.ALWAYS);
        search.setMaxWidth(WIDTH);

        Label searchLabel = new Label();
        HBox.setHgrow(searchLabel, Priority.ALWAYS);
        searchLabel.setMaxWidth(WIDTH);

        searchBox.getChildren().addAll(search, searchLabel);

        Button clear = new Button("Очистить");


//        Панель контроля(Левая часть)
        VBox control = new VBox(byScores, byNumSchool, searchBox, byHigth, clear);
        control.setSpacing(10);
        HBox.setHgrow(control, Priority.ALWAYS);
        control.setMaxWidth(Double.MAX_VALUE);


//        Таблица
        TableView<People> table = new TableView<>();
        TableColumn<People, String> firstnames = new TableColumn<People, String>("Ім'я");
        TableColumn<People, String> lastnames = new TableColumn<People, String>("Прізвище");
        TableColumn<People, Integer> scores = new TableColumn<People, Integer>("Бали ЗНО");
        TableColumn<People, Integer> numSchools = new TableColumn<People, Integer>("Номер школи");
        firstnames.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastnames.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        scores.setCellValueFactory(new PropertyValueFactory<>("score"));
        numSchools.setCellValueFactory(new PropertyValueFactory<>("numSchool"));

        table.getColumns().addAll(firstnames, lastnames, scores, numSchools);
        VBox.setVgrow(table, Priority.ALWAYS);

//        Поиск документа
        Button searchFile = new Button("Знайти документ");
        VBox.setVgrow(searchFile, Priority.ALWAYS);
        searchFile.setMaxWidth(Double.MAX_VALUE);
//        Таблица с кнопками(правая часть)
        VBox tableSetup = new VBox(table, searchFile);
        HBox.setHgrow(tableSetup, Priority.ALWAYS);
        tableSetup.setMaxWidth(Double.MAX_VALUE);

//        Функции после инициализации всех обьектов
        searchByHight.setOnAction(event -> {byHigthLabel.setText(searchByHight());});
        searchFile.setOnAction(event -> {
            try {
                table.setItems(readFile(stage));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        search.setOnAction(event -> {
            int NumSchoolToFn;
            if (byNumSchoolText.getText().isEmpty()){
                NumSchoolToFn = -1;
            } else {
                try{
                    NumSchoolToFn = Integer.parseInt(byNumSchoolText.getText());
                } catch (Exception e){ return; }
            }

            if (!byScoresText.getText().isEmpty()){
                int scoreToFn = Integer.parseInt(byScoresText.getText());
                searchLabel.setText(search(scoreToFn,NumSchoolToFn));
            }
        });
        clear.setOnAction(event -> {
            searchLabel.setText("");
            byHigthLabel.setText("");
        });
//        Основа программы
        HBox root = new HBox(20, control, tableSetup);
        root.setPadding(new Insets(15, 10, 10, 10));
        Scene scene = new Scene(root, 800, 480);
        stage.setTitle("Directory");
        stage.setScene(scene);
        stage.show();

    }

    private ObservableList<People> readFile(Stage stage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Откройте документ");
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter("Текстовый документ (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(stage);
        FileReader readerFile = new FileReader(file);
        Scanner scan = new Scanner(readerFile);
        ArrayList<People> peopleList = new ArrayList<People>();
        while (scan.hasNext()){
            String firstname = scan.next();
            String lastname = scan.next();
            int score = scan.nextInt();
            int numSchool = scan.nextInt();
            People people = new People(firstname, lastname, score, numSchool);
            peopleList.add(people);
        }
        scan.close();
        readerFile.close();
        tableInfo = FXCollections.observableArrayList(peopleList);
        return tableInfo;
    }
    private String searchByHight(){
        String newTable = new String();
        ArrayList<Integer> searched = new ArrayList<>();
        for (People people: tableInfo){
            int school = people.getNumSchool();
            if (people.getScore() == 200 && !searched.contains(school)){
                newTable += String.valueOf(school) + "\n";
                searched.add(school);
            }
        }
        return newTable;
    }
    private String search(int score, int numSchool){
        ArrayList<String> newTable = new ArrayList();
        String lastNames = new String();
        for (People people: tableInfo){
            if (people.getScore() > score && (numSchool == -1 || people.getNumSchool() == numSchool)){
                lastNames += people.getLastname() + "\n";
            }
        }
        return lastNames;
    }

}
