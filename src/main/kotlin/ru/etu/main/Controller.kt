package ru.etu.main

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.input.MouseButton
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.stage.Stage
import java.net.URL
import java.util.*
import kotlin.system.exitProcess

enum class WorkspaceSTATES{
    VERTEX,
    EDGE
}

class Controller : Initializable {


    private var x: Double = 0.0
    private var y: Double = 0.0
    private var flag : WorkspaceSTATES = WorkspaceSTATES.VERTEX


    private lateinit var graph: GraphController


    @FXML
    private lateinit var MainPane: AnchorPane

    @FXML
    private lateinit var DialogeInput: Button


    @FXML
    private lateinit var Reset: Button


    @FXML
    private lateinit var StartDijkstra: Button

    @FXML
    private lateinit var Switcher: Button


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

    @FXML
    private lateinit var questionButton: Button


    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        assert(MainPane != null) {"fx:id=\"MainPane\" was not injected: check your FXML file 'main.fxml'." }
        assert(DialogeInput != null) {"fx:id=\"DialogeInput\" was not injected: check your FXML file 'main.fxml'." }
        assert(Reset != null) {"fx:id=\"Reset\" was not injected: check your FXML file 'main.fxml'." }
        assert(StartDijkstra != null) {"fx:id=\"StartDijkstra\" was not injected: check your FXML file 'main.fxml'." }
        assert(Switcher != null) {"fx:id=\"Switcher\" was not injected: check your FXML file 'main.fxml'." }
        assert(GraphArea != null) {"fx:id=\"GraphArea\" was not injected: check your FXML file 'main.fxml'." }
        assert(Menu != null) {"fx:id=\"Menu\" was not injected: check your FXML file 'main.fxml'." }
        assert(closeButton != null) { "fx:id=\"closeButton\" was not injected: check your FXML file 'main.fxml'." }
        assert(collapseButton != null) {"fx:id=\"collapseButton\" was not injected: check your FXML file 'main.fxml'." }
        assert(topBar != null) { "fx:id=\"topBar\" was not injected: check your FXML file 'main.fxml'." }
        assert(questionButton != null) {"fx:id=\"questionButton\" was not injected: check your FXML file 'main.fxml'." }

        graph = GraphController(GraphArea)

        closeButton.onMousePressed = EventHandler {
            it.consume()
            Platform.exit()
            exitProcess(0)
        }
        collapseButton.onMousePressed = EventHandler {
            it.consume()
            (MainPane.scene.window as Stage).isIconified = true
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

        Switcher.onAction = EventHandler {
            it.consume()
            flag = if (flag == WorkspaceSTATES.VERTEX) {
                Switcher.text = "Add Vertex"
                WorkspaceSTATES.EDGE
            } else {
                Switcher.text = "Add Edge"
                WorkspaceSTATES.VERTEX
            }
            graph.state = flag
        }


        GraphArea.onMouseClicked = EventHandler {
            it.consume()
            if (!it.isStillSincePress)
                return@EventHandler
            if (it.button == MouseButton.PRIMARY && flag.equals(WorkspaceSTATES.VERTEX)){
                val vertex = graph.createVertex(it.sceneX, it.sceneY) ?: return@EventHandler
                graph.drawVertex(vertex)
            }
        }
        Reset.onMousePressed = EventHandler {
            it.consume()
            graph.clear()
        }
    }
}