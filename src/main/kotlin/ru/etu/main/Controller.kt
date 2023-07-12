package ru.etu.main

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.InputEvent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.io.File
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
    private lateinit var TextInfo: Label


    @FXML
    private lateinit var StartDijkstra: Button

    @FXML
    private lateinit var Switcher: Button

    @FXML
    private lateinit var DialogeInput: Button

    @FXML
    private lateinit var Reset: Button

    @FXML
    private lateinit var NextStep: Button


    @FXML
    private lateinit var Skip: Button


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
        TextInfo.text = ""
        NextStep.isVisible = false
        Skip.isVisible = false
        graph.afterAlgorithm()
        handlerBlocker = false
        GraphArea.removeEventFilter(InputEvent.ANY, handlerAll)
    }

    private fun showHelp(){
        val dialog = Alert(Alert.AlertType.INFORMATION)
        dialog.x = MainPane.scene.window.x + MainPane.width / 2
        dialog.y = MainPane.scene.window.y + MainPane.height / 4
        dialog.initStyle(StageStyle.UNDECORATED)
        dialog.title = "Help info"
        dialog.contentText = "\tAbout 'Dijkstra\'s Algorithm Visualizer' program\n" +
                "- Button “Start Dijkstra” - starts the algorithm if the initial vertex has been selected, otherwise it gives an error message.\n" +
                "- Button “Add vertex/Add edge” - switches the working space mode.\n" +
                "- Button “Dialog Input” - provides the ability to enter a graph through a dialog box\n" +
                "- “Reset” button - clears the workspace (deletes the graph).\n" +
                "To the right of the main menu is the workspace, which, depending on the mode, " +
                "allows you to interactively arrange vertices and connect them with edges. " +
                "When you right-click on a vertex, it is possible to rename and delete it through the context menu. " +
                "In the same way, using the right mouse button on edge, you can delete an edge or change its weight.\n\n" +
                "At the end of the algorithm, next to each vertex, the minimum weight of the path from the initial vertex to " +
                "this one will be displayed, if the path between them exists, otherwise the infinity symbol."
        dialog.showAndWait()
    }

    private fun showEnd(){
        val dialog = Dialog<ButtonType>()
        dialog.isResizable=false
        dialog.headerText= "The algorithm has completed its work, the values are obtained:"
        dialog.contentText= graph.getInfo()
        dialog.dialogPane.scene.window.onCloseRequest = EventHandler { dialog.hide() }
        dialog.showAndWait()
    }

    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        assert(MainPane != null) {"fx:id=\"MainPane\" was not injected: check your FXML file 'main.fxml'." }

        assert(Menu != null) {"fx:id=\"Menu\" was not injected: check your FXML file 'main.fxml'." }
        assert(StartDijkstra != null) {"fx:id=\"StartDijkstra\" was not injected: check your FXML file 'main.fxml'." }
        assert(Switcher != null) {"fx:id=\"Switcher\" was not injected: check your FXML file 'main.fxml'." }
        assert(DialogeInput != null) {"fx:id=\"DialogeInput\" was not injected: check your FXML file 'main.fxml'." }
        assert(Reset != null) {"fx:id=\"Reset\" was not injected: check your FXML file 'main.fxml'." }
        assert(NextStep != null) {"fx:id=\"NextStep\" was not injected: check your FXML file 'main.fxml'." }
        assert(Skip != null) {"fx:id=\"Skip\" was not injected: check your FXML file 'main.fxml'." }

        assert(GraphArea != null) {"fx:id=\"GraphArea\" was not injected: check your FXML file 'main.fxml'." }

        assert(topBar != null) { "fx:id=\"topBar\" was not injected: check your FXML file 'main.fxml'." }
        assert(closeButton != null) { "fx:id=\"closeButton\" was not injected: check your FXML file 'main.fxml'." }
        assert(collapseButton != null) {"fx:id=\"collapseButton\" was not injected: check your FXML file 'main.fxml'." }
        assert(questionButton != null) {"fx:id=\"questionButton\" was not injected: check your FXML file 'main.fxml'." }
        assert(TextInfo != null) {"fx:id=\"TextInfo\" was not injected: check your FXML file 'main.fxml'." }

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
        questionButton.onAction = EventHandler {
            it.consume()
            showHelp()
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
            it.consume()
            if (handlerBlocker) {
                unlock()
                return@EventHandler
            }
            // Set block
            handlerBlocker = true
            StartDijkstra.text = "Unlock"
            GraphArea.addEventFilter(InputEvent.ANY, handlerAll)
            TextInfo.text = "Initializing Dijkstra"
            NextStep.isVisible = true
            Skip.isVisible = true

            if (!graph.preInitAlgorithm()){
                // Unlock, because no selected vertex
                unlock()
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "No start vertex selected"
                alert.contentText = "Go into add edges mode and select a vertex.\nThen press 'Start Dijkstra button'"
                alert.showAndWait()
                return@EventHandler
            }
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
        NextStep.onAction = EventHandler {
            if (TextInfo.text == "Algorithm finished"){showEnd(); return@EventHandler}
            TextInfo.text = graph.makeStep()
        }
        Skip.onAction = EventHandler {
            TextInfo.text = graph.makeStep()
            while (TextInfo.text != "Algorithm finished"){TextInfo.text = graph.makeStep()}
            showEnd()
        }
        DialogeInput.onMousePressed = EventHandler {
            val fxmlLoader = FXMLLoader(MainApplication::class.java.getResource("InputWindow.fxml"))
            val dialogPane = fxmlLoader.load<DialogPane>()
            val dialogController = fxmlLoader.getController<InputWindow>()
            val dialog = Dialog<ButtonType>()
            dialog.dialogPane = dialogPane
            dialog.title = "Input from Dialog"
            val dialogWindow = dialogPane.scene.window
            dialogWindow.onCloseRequest = EventHandler {
                dialogWindow.hide()
            }
            dialog.showAndWait()
            if (dialogController.getFlag()){
                graph.setGraph(dialogController.getVertexList(), dialogController.getEdgeList())
            }
        }

        MainPane.addEventHandler(KeyEvent.KEY_PRESSED) {
            if (it.code.equals(KeyCode.F1)) {
                it.consume()
                showHelp()
            }
        }
    }
}