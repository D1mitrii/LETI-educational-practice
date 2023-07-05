package ru.etu.main

import javafx.event.EventHandler
import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.TextArea
import javafx.stage.Stage
import java.lang.Exception

class InputWindow{


    @FXML
    private lateinit var TextArea: TextArea

    @FXML
    private lateinit var approveButton: Button


    @FXML
    fun initialize() {
        assert(TextArea != null) {"fx:id=\"TextArea\" was not injected: check your FXML file 'InputWindow.fxml'." }
        assert(approveButton != null) {"fx:id=\"approveButton\" was not injected: check your FXML file 'InputWindow.fxml'." }
        approveButton.onMousePressed = EventHandler {

        }
    }

}
