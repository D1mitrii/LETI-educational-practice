package ru.etu.main

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import java.net.URL
import java.util.*

class Controller : Initializable {


    private var x: Double = 0.0
    private var y: Double = 0.0

    @FXML
    private lateinit var closeButton: Button

    @FXML
    private lateinit var topBar: AnchorPane

    @FXML
    private lateinit var collapseButton: Button


    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        assert(closeButton != null) { "fx:id=\"closeButton\" was not injected: check your FXML file 'main.fxml'." }
        assert(collapseButton != null) {"fx:id=\"collapseButton\" was not injected: check your FXML file 'main.fxml'." }
        assert(topBar != null) { "fx:id=\"topBar\" was not injected: check your FXML file 'main.fxml'." }
        closeButton.onMousePressed = EventHandler {
            val stage = (it.source as Button).scene.window
            stage.hide()
        }
        topBar.onMousePressed = EventHandler {
            x = it.sceneX
            y = it.sceneY
        }
        topBar.onMouseDragged = EventHandler {
            val stage = (it.source as AnchorPane).scene.window
            stage.x = (it.screenX - x)
            stage.y = (it.screenY - y)
        }
    }

}