module ru.etu.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens ru.etu.main to javafx.fxml;
    exports ru.etu.main;
}