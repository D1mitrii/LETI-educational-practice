package ru.etu.main

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Cursor
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TextInputDialog
import javafx.scene.input.MouseButton
import javafx.scene.layout.Pane
import javafx.stage.StageStyle
import java.util.*

class GraphController(var pane: Pane){

    @FXML
    private var renameOption: MenuItem = MenuItem("Rename")

    @FXML
    private var deleteOption: MenuItem = MenuItem("Delete")


    @FXML
    private var contextMenu: ContextMenu = ContextMenu(renameOption, deleteOption)

    private val startArea: Pair<Double, Double> = Pair(pane.layoutX, pane.layoutY)
    private val width: Double = 980.0
    private val height: Double = 624.0

    private val vertexArray: MutableList<Vertex> = mutableListOf()

    private val names: MutableMap<String, Boolean> = mutableMapOf()

    init {
        for (ch in 'A'..'Z'){
            names[ch.toString()] = true
        }
    }

    fun isNameAvailable(name: String) : Boolean {
        return names[name] ?: true
    }

    private fun getName(): String{
        for (ch in 'A'..'Z'){
            val string = ch.toString()
            if (names[string] == true){
                names[string] = false
                return string
            }
        }
        return "-"
    }
    //TODO Добавить возможность передвигать за название вершины (мб вершину сделать как Group)
    fun createVertex(x: Double, y: Double) : Vertex? {
        val name = getName()
        if (name == "-") return null

        val vertex = Vertex(name, x - startArea.first, y - startArea.second)
        vertex.onMouseEntered = EventHandler {
            vertex.cursor = Cursor.HAND
        }
        vertex.onMouseDragged = EventHandler {
            it.consume()
            if (!it.isPrimaryButtonDown)
                return@EventHandler
            var newX = it.sceneX - startArea.first
            if (newX < 0){
                newX = 0.0
            }
            else if (newX >= width){
                newX = width
            }
            var newY = it.sceneY - startArea.second
            if (newY < 0){
                newY = 0.0
            }
            else if (newY >= height){
                newY = height
            }
            vertex.changePosition(newX, newY)
        }
        vertex.onMouseClicked = EventHandler {
            it.consume()
            if (it.button != MouseButton.SECONDARY) return@EventHandler
            contextMenu.show(pane, it.screenX, it.screenY)
            renameOption.onAction = EventHandler {
                if (contextMenu.isShowing) {
                    contextMenu.hide()
                }
                val dialogRenameVertex = TextInputDialog()
                dialogRenameVertex.title = "Rename vertex"
                dialogRenameVertex.headerText = "Enter vertex name:"
                dialogRenameVertex.initStyle(StageStyle.UNDECORATED)
                dialogRenameVertex.isResizable = false
                dialogRenameVertex.contentText = "Name:"
                val newName: Optional<String> = dialogRenameVertex.showAndWait()
                if (newName.isEmpty) return@EventHandler
                if (isNameAvailable(newName.get())){
                    names[vertex.name]
                    names[newName.get()] = false
                    vertex.updateName(newName.get())
                }
            }
            deleteOption.onAction = EventHandler {
                if (contextMenu.isShowing) {
                    contextMenu.hide()
                }
                deleteVertex(vertex)
            }
        }
        drawVertex(vertex)
        vertexArray.add(vertex)
        return vertex
    }

    fun drawVertex(vertex: Vertex) {
        pane.children.addAll(vertex, vertex.text)
    }

    fun deleteVertex(vertex: Vertex){
        names[vertex.name] = true
        vertex.edges.forEach {
            it.start!!.deleteEdge(it)
            it.end!!.deleteEdge(it)
            pane.children.removeAll(it)
        }
        pane.children.removeAll(vertex, vertex.text)
    }
}