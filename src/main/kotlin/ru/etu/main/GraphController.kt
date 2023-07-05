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


const val WorkspaceWIDTH = 980.0
const val WorkspaceHEIGHT = 624.0

class GraphController(var pane: Pane){

    @FXML
    private var renameOption: MenuItem = MenuItem("Rename")

    @FXML
    private var deleteOption: MenuItem = MenuItem("Delete")

    var state: WorkspaceSTATES = WorkspaceSTATES.VERTEX
        set(value) {
            field = value
            selectedVertex = null
        }

    private var selectedVertex: Vertex? = null


    @FXML
    private var contextMenu: ContextMenu = ContextMenu(renameOption, deleteOption)

    private val startArea: Pair<Double, Double> = Pair(pane.layoutX, pane.layoutY)

    private val vertexArray: MutableList<Vertex> = mutableListOf()
    private val edgeArray: MutableList<Edge> = mutableListOf()

    private val names: MutableMap<String, Boolean> = mutableMapOf()

    init {
        freeNames()
    }

    fun isNameAvailable(name: String) : Boolean {
        return names[name] ?: true
    }

    private fun freeNames(){
        names.clear()
        for (ch in 'A'..'Z'){
            names[ch.toString()] = true
        }
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

    private fun checkCoordinate(cord: Double, max: Double) : Double{
        var newCord = cord
        if (cord < 0){
            newCord = 0.0
        }
        else if (cord >= max){
            newCord = max
        }
        return newCord
    }

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
            val newX = checkCoordinate(it.sceneX - startArea.first, WorkspaceWIDTH)
            val newY = checkCoordinate(it.sceneY - startArea.second, WorkspaceHEIGHT)
            vertex.changePosition(newX, newY)
        }
        vertex.onMouseClicked = EventHandler {
            it.consume()

            if (it.button == MouseButton.PRIMARY && state == WorkspaceSTATES.EDGE){
                selectVertex(vertex)
                return@EventHandler
            }
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
        vertex.text.onMouseDragged = vertex.onMouseDragged
        vertexArray.add(vertex)
        drawVertex(vertex)
        vertexArray.reverse()
        return vertex
    }

    fun drawVertex(vertex: Vertex) {
        pane.children.addAll(vertex, vertex.text, vertex.minPath)
    }

    fun deleteVertex(vertex: Vertex){
        names[vertex.name] = true
        for (edge in vertex.edges){
            pane.children.removeAll(edge, edge.weightText)
            edgeArray.remove(edge)
        }
        pane.children.removeAll(vertex, vertex.text)
        vertexArray.remove(vertex)
    }

    private fun selectVertex(vertex: Vertex){
        println(edgeArray)
        // if it already selected, unselect
        if (vertex.id == "VertexSelected") {
            vertex.id = "Vertex"
            selectedVertex = null
            return
        }
        // check if it's first time selection
        vertex.id = "VertexSelected"
        if (selectedVertex == null){
            selectedVertex = vertex
            return
        }
        // on second selected vertex add edge

        val newEdge = Edge()
        newEdge.addStart(selectedVertex!!)
        newEdge.addEnd(vertex)
        edgeArray.add(newEdge)

        selectedVertex!!.edges.add(newEdge)
        vertex.edges.add(newEdge)

        // Edge created now unselect vertexes
        selectedVertex!!.id = "Vertex"
        vertex.id = "Vertex"
        selectedVertex = null

        drawGraph()
    }

    fun drawGraph(){
        pane.children.clear()
        edgeArray.forEach { pane.children.addAll(it, it.weightText) }
        vertexArray.forEach { pane.children.addAll(it, it.text) }
    }

    fun clear(){
        pane.children.clear()
        edgeArray.clear()
        vertexArray.clear()
        freeNames()
    }
}