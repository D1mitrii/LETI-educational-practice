package ru.etu.main

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.input.MouseButton
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import java.net.URL
import java.util.*

class Controller : Initializable {


    private var x: Double = 0.0
    private var y: Double = 0.0

    private lateinit var graph: GraphController

    @FXML
    private lateinit var closeButton: Button

    @FXML
    private lateinit var topBar: AnchorPane

    @FXML
    private lateinit var collapseButton: Button

    @FXML
    private lateinit var Menu: AnchorPane


    @FXML
    private lateinit var GraphArea: Pane

    private var flag: Boolean = true

    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        assert(GraphArea != null) {"fx:id=\"GraphArea\" was not injected: check your FXML file 'main.fxml'." }
        assert(Menu != null) {"fx:id=\"Menu\" was not injected: check your FXML file 'main.fxml'." }
        assert(closeButton != null) { "fx:id=\"closeButton\" was not injected: check your FXML file 'main.fxml'." }
        assert(collapseButton != null) {"fx:id=\"collapseButton\" was not injected: check your FXML file 'main.fxml'." }
        assert(topBar != null) { "fx:id=\"topBar\" was not injected: check your FXML file 'main.fxml'." }

        graph = GraphController(GraphArea)
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

        GraphArea.onMouseClicked = EventHandler {
            //TODO Убрать возможность добавления вершины при нажатии на одну из уже существующих
            if (it.button == MouseButton.PRIMARY){
                GraphArea.children.add(graph.createVertex(it.sceneX, it.sceneY))
            }
        }
    }
}