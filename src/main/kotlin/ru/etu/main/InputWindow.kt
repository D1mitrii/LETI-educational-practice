package ru.etu.main

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextArea

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

    }

}
