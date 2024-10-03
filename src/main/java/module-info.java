module com.example.newsparser {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;


    opens com.example.newsparser to javafx.fxml;
    exports com.example.newsparser;
}