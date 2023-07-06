package ru.etu.main

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import java.net.URL
import java.util.*


class InputWindow {

    @FXML
    private lateinit var resources: ResourceBundle

    @FXML
    private lateinit var location: URL

    @FXML
    private lateinit var TextArea: TextArea

    @FXML
    private lateinit var approveButton: Button

    @FXML
    fun initialize() {
        assert(TextArea != null) {"fx:id=\"TextArea\" was not injected: check your FXML file 'InputWndow.fxml'." }
        assert(approveButton != null) {"fx:id=\"approveButton\" was not injected: check your FXML file 'InputWndow.fxml'." }


        approveButton.onMousePressed = EventHandler {
            val Text = TextArea.getText()
            val lines : List<String> = Text.lines()
            val N = lines[0].toInt()
            for (i in 1 .. N)
            {
                val elems = lines[i].split("\\s+".toRegex()).toTypedArray()
                if (elems[0].length !in 1..2) {
                    val alert = Alert(AlertType.ERROR)

                    alert.title = "Error alert"
                    alert.headerText = "Wrong name!"
                    alert.contentText = "Имя вершины не соответствует правилам!"

                    alert.showAndWait()
                }
                if (elems[1].toInt() !in 0..980) {
                    val alert = Alert(AlertType.ERROR)

                    alert.title = "Error alert"
                    alert.headerText = "Error X!"
                    alert.contentText = "Координата Х задана неверно"

                    alert.showAndWait()
                }
                if (elems[2].toInt() !in 0..624){
                    val alert = Alert(AlertType.ERROR)

                    alert.title = "Error alert"
                    alert.headerText = "Error Y!"
                    alert.contentText = "Координата Y задана неверно"

                    alert.showAndWait()
                }
            }
            for (i in N + 1 until lines.size)
            {
                val elems = lines[i].split("\\s+".toRegex()).toTypedArray()
                if (elems[0] == elems[1]) {
                    val alert = Alert(AlertType.ERROR)

                    alert.title = "Error alert"
                    alert.headerText = "Loop error!"
                    alert.contentText = "В графе не должно быть петель!"

                    alert.showAndWait()
                    }
                if (elems[2].toInt() < 0){
                    val alert = Alert(AlertType.ERROR)

                    alert.title = "Error alert"
                    alert.headerText = "Edge value error!"
                    alert.contentText = "Вес ребра не может быть отрицательным!"

                    alert.showAndWait()
                }
            }
        }
    }
}
