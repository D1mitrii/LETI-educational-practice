package ru.etu.main

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextInputDialog
import javafx.scene.input.InputEvent
import javafx.scene.input.MouseButton
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit
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



    private var handlerBlocker: Boolean = false
    private val handlerAll = EventHandler<InputEvent>{it.consume()}

    private fun unlock(){
        StartDijkstra.text = "Start Dijkstra"
        graph.afterAlgorithm()
        handlerBlocker = false
        GraphArea.removeEventFilter(InputEvent.ANY, handlerAll)
    }
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

        closeButton.onAction = EventHandler {
            it.consume()
            Platform.exit()
            exitProcess(0)
        }
        collapseButton.onAction = EventHandler {
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

        StartDijkstra.onAction = EventHandler{
            if (handlerBlocker) {
                unlock()
                return@EventHandler
            }
            // Set block
            handlerBlocker = true
            StartDijkstra.text = "Unlock"
            GraphArea.addEventFilter(InputEvent.ANY, handlerAll)

            if (!graph.preInitAlgorithm()){
                // Unlock, because no selected vertex
                unlock()
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "No start vertex selected"
                alert.contentText = "Go into add edges mode and select a vertex.\nThen press 'Start Dijkstra button'"
                alert.showAndWait()
                return@EventHandler
            }
            graph.dijkstra()
        }

        Switcher.onAction = EventHandler {
            it.consume()
            if (handlerBlocker) return@EventHandler
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
            if (handlerBlocker) return@EventHandler
            if (!it.isStillSincePress)
                return@EventHandler
            if (it.button == MouseButton.PRIMARY && flag.equals(WorkspaceSTATES.VERTEX)){
                graph.createVertex(it.sceneX, it.sceneY) ?: return@EventHandler
            }
        }
        Reset.onAction = EventHandler {
            it.consume()
            if (handlerBlocker) return@EventHandler
            graph.clear()
        }
    }
}