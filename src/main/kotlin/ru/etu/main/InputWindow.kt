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

    private fun alert_throw(headerText : String, contentText : String)
    {
        val alert = Alert(AlertType.ERROR)

        alert.title = "Error alert"
        alert.headerText = headerText
        alert.contentText = contentText

        alert.showAndWait()
    }

    @FXML
    fun initialize() {
        assert(TextArea != null) {"fx:id=\"TextArea\" was not injected: check your FXML file 'InputWndow.fxml'." }
        assert(approveButton != null) {"fx:id=\"approveButton\" was not injected: check your FXML file 'InputWndow.fxml'." }
        assert(openButton != null) {"fx:id=\"openButton\" was not injected: check your FXML file 'InputWindow.fxml'." }


        approveButton.onMousePressed = EventHandler {
            val Text = TextArea.getText()
            var N = 1
            val lines : List<String> = Text.lines()
            if (lines[0].isEmpty() || lines[0][0].isWhitespace())
            {
                alert_throw("Empty field!", "The field is empty!")

                return@EventHandler
            }
            else if (!lines[0][0].isDigit())
            {
                alert_throw("Wrong input!", "Digit must be first, not the letter!")

                return@EventHandler
            } else
                N = lines[0].toInt()
            for (i in 1 until N) {
                val elems = lines[i].split("\\s+".toRegex()).toTypedArray()
                if (elems[0].length !in 1..2) {
                    alert_throw("Wrong name!", "The name of the vertex is incorrect!")

                    return@EventHandler
                }
                if (elems[1].toInt() !in 0..980) {
                    alert_throw("Error X!", "The X coordinate is incorrect!")

                    return@EventHandler
                }
                if (elems[2].toInt() !in 0..624) {
                    alert_throw("Error Y!", "The Y coordinate is incorrect!")

                    return@EventHandler
                }
            }

            for (i in N until lines.size - 1) {
                val elems = lines[i].split("\\s+".toRegex()).toTypedArray()
                if (elems[0] == elems[1]) {
                    alert_throw("Loop error!", "There must be no loop in graph!")

                    return@EventHandler
                }
                if (elems[0].length !in 1..2 || elems[1].length !in 1..2) {
                    alert_throw("Wrong name!", "The name of the vertex is incorrect!")

                    return@EventHandler
                }
                if (elems[2].toInt() !in 0..50) {
                    alert_throw("Edge value error!", "The value of the edge is incorrect!")

                    return@EventHandler
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
                alert_throw("Open error!", "Could not open the file!")
            }
        }
    }
}
