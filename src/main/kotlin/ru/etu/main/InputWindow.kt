package ru.etu.main

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.stage.FileChooser
import java.io.File
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
    private lateinit var openButton: Button

    @FXML
    fun initialize() {
        assert(TextArea != null) {"fx:id=\"TextArea\" was not injected: check your FXML file 'InputWndow.fxml'." }
        assert(approveButton != null) {"fx:id=\"approveButton\" was not injected: check your FXML file 'InputWndow.fxml'." }
        assert(openButton != null) {"fx:id=\"openButton\" was not injected: check your FXML file 'InputWindow.fxml'." }


        approveButton.onMousePressed = EventHandler {
            val Text = TextArea.getText()
            var N = 1
            var flag = true
            val lines : List<String> = Text.lines()
            if (lines[0].isEmpty() || lines[0][0].isWhitespace())
            {
                val alert = Alert(AlertType.ERROR)

                alert.title = "Error alert"
                alert.headerText = "Empty field!"
                alert.contentText = "The field is empty!"
                flag = false

                alert.showAndWait()
            }
            else if (lines[0][0].isLetter())
            {
                val alert = Alert(AlertType.ERROR)

                alert.title = "Error alert"
                alert.headerText = "Wrong input!"
                alert.contentText = "Digit must be first, not the letter!"
                flag = false

                alert.showAndWait()
            } else
                N = lines[0].toInt()
            if (flag) {
                for (i in 1..N) {
                    val elems = lines[i].split("\\s+".toRegex()).toTypedArray()
                    if (elems[0].length !in 1..2) {
                        val alert = Alert(AlertType.ERROR)

                        alert.title = "Error alert"
                        alert.headerText = "Wrong name!"
                        alert.contentText = "The name of the vertex is incorrect!"
                        flag = false

                        alert.showAndWait()
                    }
                    if (elems[1].toInt() !in 0..980) {
                        val alert = Alert(AlertType.ERROR)

                        alert.title = "Error alert"
                        alert.headerText = "Error X!"
                        alert.contentText = "The X coordinate is incorrect!"
                        flag = false

                        alert.showAndWait()
                    }
                    if (elems[2].toInt() !in 0..624) {
                        val alert = Alert(AlertType.ERROR)

                        alert.title = "Error alert"
                        alert.headerText = "Error Y!"
                        alert.contentText = "The X coordinate is incorrect!"
                        flag = false

                        alert.showAndWait()
                    }
                    if (!flag)
                        break
                }
            }
            if (flag) {
                for (i in N + 1 until lines.size) {
                    val elems = lines[i].split("\\s+".toRegex()).toTypedArray()
                    if (elems[0] == elems[1]) {
                        val alert = Alert(AlertType.ERROR)

                        alert.title = "Error alert"
                        alert.headerText = "Loop error!"
                        alert.contentText = "There must be no loop in graph!"
                        flag = false

                        alert.showAndWait()
                    }
                    if (elems[2].toInt() !in 0..50) {
                        val alert = Alert(AlertType.ERROR)

                        alert.title = "Error alert"
                        alert.headerText = "Edge value error!"
                        alert.contentText = "The value of the edge is incorrect!"
                        flag = false

                        alert.showAndWait()
                    }
                    if (flag)
                        break
                }
            }
        }

        openButton.onMousePressed = EventHandler{
            val fileChooser = FileChooser()
            val file = fileChooser.showOpenDialog(null)
            if (file != null)
            {
                TextArea.clear()
                for (line in file.readLines()) {
                    if (!line.isEmpty())
                        TextArea.appendText(line + "\n")
                }
            }
            else
            {
                val alert = Alert(AlertType.ERROR)

                alert.title = "Error alert"
                alert.headerText = "Open error!"
                alert.contentText = "Could not open the file!"

                alert.showAndWait()
            }
        }
    }
}
