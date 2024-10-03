package com.example.newsparser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.jsoup.nodes.Document;
import org.jsoup.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Label lbl = new Label();
        TextField textField = new TextField();
        textField.setPrefColumnCount(11);
        Button btn = new Button("GO!");
        //перенаправление текста из поля:
        btn.setOnAction(event -> lbl.setText(parse(textField.getText())));
        FlowPane root = new FlowPane(Orientation.HORIZONTAL, 10, 10, textField, btn, lbl);
        root.setAlignment(Pos.TOP_LEFT);
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("News parser");
        stage.setScene(scene);
        stage.show();
    }

    public String parse(String url) {
        if (url.length() == 0) {
            return "";
        }
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("http://www.google.com")
                    .get();
        } catch (Exception e){
            e.printStackTrace();
        }
        //отбор содержимого под тегами item и title и сохранение в map
        Elements news = doc.select("item > title");
        Map<String, Integer> map = new TreeMap<>();
        String[] str;
        for (Element element : news) {
            str = element.text().split(" ");
            for (String s: str) {
                if (s.length() < 3) {
                    continue;
                }
                if (map.containsKey(s.toLowerCase())) {
                    map.put(s.toLowerCase(), map.get(s.toLowerCase()) + 1);
                } else {
                    map.put(s.toLowerCase(), 1);
                }
            }
        }

        //перебор ранее сохраненненного в sortedMap где сортировка по значениям
        Map<String, Integer> sortedMap = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        //сохранение в строку
        StringBuilder res = new StringBuilder();
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            if (entry.getValue() > 4)
                res.append(entry.getKey()).append(" = ").append(entry.getValue()).append('\n');
        }
        return res.toString();
    }

    public static void main(String[] args) {
        launch();
    }
}

// https://lenta.ru/rss
// https://ria.ru/export/rss2/archive/index.xml
// https://www.vedomosti.ru/rss/news.xml
