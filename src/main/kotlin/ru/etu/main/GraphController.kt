package ru.etu.main

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Cursor
import javafx.scene.control.Alert
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
    private var reweightOption: MenuItem = MenuItem("Change Weight")
    @FXML
    private var deleteVertexOption: MenuItem = MenuItem("Delete")
    @FXML
    private var deleteEdgeOption: MenuItem = MenuItem("Delete")

    @FXML
    private var contextMenuVertex: ContextMenu = ContextMenu(renameOption, deleteVertexOption)

    @FXML
    private var contextMenuEdge: ContextMenu = ContextMenu(reweightOption, deleteEdgeOption)

    @FXML
    private var dialog: TextInputDialog = TextInputDialog()
    @FXML
    private var alert: Alert = Alert(Alert.AlertType.WARNING)

    var state: WorkspaceSTATES = WorkspaceSTATES.VERTEX
        set(value) {
            field = value
            selectedVertex = null
        }

    private var selectedVertex: Vertex? = null

    private val startArea: Pair<Double, Double> = Pair(pane.layoutX, pane.layoutY)

    private val vertexArray: MutableList<Vertex> = mutableListOf()
    private val edgeArray: MutableList<Edge> = mutableListOf()

    private val names: MutableMap<String, Boolean> = mutableMapOf()

    private var currentVertex: Vertex? = null
    private var prevEdge: Edge? = null
    private var positionInVertex: Int = 0

    private var queue: PriorityQueue<Vertex> = PriorityQueue<Vertex>()


    init {
        freeNames()
        dialog.initStyle(StageStyle.UNDECORATED)
        dialog.isResizable = false

        alert.initStyle(StageStyle.UNDECORATED)
        alert.isResizable = false

    }

    fun preInitAlgorithm() : Boolean {
        if(selectedVertex == null){
            return false
        }
        for (vertex in vertexArray){
            vertex.currectPath = Int.MAX_VALUE
            vertex.isUsed = false
            pane.children.add(vertex.minPath)
        }
        selectedVertex!!.currectPath = 0
        selectedVertex!!.isUsed = true


        for (edge in selectedVertex!!.edges){
            val neighbor = edge.getVertex(selectedVertex!!)
            neighbor.currectPath = edge.weight
            queue.add(neighbor)
        }
        return true
    }

    private fun checkEdge() : String{
        prevEdge?.id = "Edge"
        while(positionInVertex < currentVertex!!.edges.size){
            prevEdge = currentVertex!!.edges[positionInVertex]
            positionInVertex++
            val neighbor = prevEdge!!.getVertex(currentVertex!!)
            if (neighbor.isUsed) continue
            prevEdge!!.id = "EdgeCheck"
            queue.add(neighbor)
            if (currentVertex!!.currectPath + prevEdge!!.weight < neighbor.currectPath){
                val prevPath = neighbor.minPath.text
                neighbor.currectPath = currentVertex!!.currectPath + prevEdge!!.weight
                return "Updating the shortest path to the vertex \'${neighbor.name}\' from $prevPath to ${neighbor.currectPath}"
            }
            return "The shortest path to the vertex \'${neighbor.name}\' could not be updated"
        }
        currentVertex!!.isUsed = true
        val name = currentVertex!!.name
        currentVertex = null
        return "Mark vertex '${name}' as viewed"
    }

    fun makeStep() : String {
        queue.removeIf { it.isUsed }
        if (currentVertex == null && queue.isEmpty()) return "Algorithm finished"

        if (currentVertex == null){
            currentVertex = queue.remove()
            currentVertex!!.id = "VertexSelected"
            positionInVertex = 0
        }

        return checkEdge()
    }


    fun afterAlgorithm(){
        vertexArray.forEach {
            it.currectPath = Int.MAX_VALUE
            it.id = "Vertex"
            pane.children.remove(it.minPath)
        }
        edgeArray.forEach {
            it.id = "Edge"
        }
        selectedVertex?.id = "Vertex"
        selectedVertex = null
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

    private fun setHandlersVertex(vertex: Vertex){
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

            contextMenuVertex.show(pane.parent.scene.window, it.screenX, it.screenY)
            renameOption.onAction = EventHandler {
                renameVertex(vertex)
            }
            deleteVertexOption.onAction = EventHandler {
                deleteVertex(vertex)
            }
        }
        vertex.text.onMouseDragged = vertex.onMouseDragged
    }

    fun createVertex(x: Double, y: Double) : Vertex? {
        val name = getName()
        if (name == "-") return null

        val vertex = Vertex(name, x - startArea.first, y - startArea.second)
        setHandlersVertex(vertex)
        vertexArray.add(vertex)
        drawVertex(vertex)
        return vertex
    }

    private fun drawVertex(vertex: Vertex) {
        pane.children.addAll(vertex, vertex.text)
    }

    private fun deleteVertex(vertex: Vertex){
        names[vertex.name] = true
        println(vertex.edges)
        val edgeCopy = vertex.edges.toList()
        edgeCopy.forEach {
            deleteEdge(it)
        }
        pane.children.removeAll(vertex, vertex.text)
        vertexArray.remove(vertex)
    }

    private fun deleteEdge(edge: Edge){
        // Delete from Vertex model
        edge.end!!.edges.remove(edge)
        edge.start!!.edges.remove(edge)
        edgeArray.remove(edge)
        // Delete from ui
        pane.children.removeAll(edge, edge.weightText)
    }

    private fun handleWeight(newWeight: Optional<String>) : Int? {
        dialog.editor.clear()
        if (newWeight.isEmpty) return null
        val num = newWeight.get().toIntOrNull()
        if (num == null || num < 0 || num > 50) {
            alert.title = "Uncorrected weight"
            alert.contentText = "Edge weight must be integer in [0, 50]"
            alert.showAndWait()
            return null
        }
        return num
    }

    private fun checkAndUnselect(vertex: Vertex) : Boolean{
        if (vertex.id == "VertexSelected") {
            vertex.id = "Vertex"
            return true
        }
        return false
    }

    private fun selectVertex(vertex: Vertex){

        // if it already selected, unselect
        if (checkAndUnselect(vertex)) return

        // check if it's first time selection
        vertex.id = "VertexSelected"
        if (selectedVertex == null){
            selectedVertex = vertex
            return
        }

        // check if edge already exist
        vertex.edges.forEach {
            if (it.getVertex(vertex) == selectedVertex){
                unselectVertex(vertex)
                alert.title = "Unable to create edge"
                alert.contentText = "The edge already exists right click on it to reweight it"
                alert.showAndWait()
                return
            }
        }
        // on second selected vertex create edge
        val edge = edgeCreation(vertex)
        if (edge != null) {
            setHandlersEdge(edge)
        }

        // Edge created now unselect vertexes
        unselectVertex(vertex)

        drawGraph()
    }

    private fun unselectVertex(vertex: Vertex){
        selectedVertex!!.id = "Vertex"
        vertex.id = "Vertex"
        selectedVertex = null
    }

    private fun setHandlersEdge(edge: Edge){
        edge.onMouseClicked = EventHandler {
            it.consume()
            if (it.button != MouseButton.SECONDARY) return@EventHandler
            contextMenuEdge.show(pane.parent.scene.window, it.screenX, it.screenY)
            reweightOption.onAction = EventHandler {
                reweight(edge)
            }
            deleteEdgeOption.onAction = EventHandler {
                deleteEdge(edge)
            }
            return@EventHandler
        }
        edge.weightText.onMouseClicked = edge.onMouseClicked
    }

    private fun reweight(edge: Edge){
        dialog.title = "Reweight edge"
        dialog.headerText = "Enter new edge weight (Int in [0, 50]):"
        dialog.contentText = "Weight:"
        val newWeight: Optional<String> = dialog.showAndWait()
        val weightNum = handleWeight(newWeight) ?: return
        edge.changeWeight(weightNum)
    }

    private fun renameVertex(vertex: Vertex) {
        dialog.title = "Rename vertex"
        dialog.headerText = "Enter vertex name (length in [1, 2]):"
        dialog.contentText = "Name:"
        val newName: Optional<String> = dialog.showAndWait()
        dialog.editor.clear()
        if (newName.isEmpty) return
        alert.title = "Uncorrected vertex name"
        if (newName.get().isEmpty() || newName.get().length > 2){
            alert.contentText = "Vertex name length must be in [1, 2]"
            alert.showAndWait()
            return
        }
        if (isNameAvailable(newName.get())){
            names[vertex.name] = true
            names[newName.get()] = false
            vertex.updateName(newName.get())
        }
        else{
            alert.contentText = "Vertex name already in use, choose another"
            alert.showAndWait()
            return
        }
    }

    private fun edgeCreation(vertex: Vertex) : Edge? {
        dialog.title = "Set weight edge"
        dialog.headerText = "Enter new edge weight (Int in [0, 50]):"
        dialog.contentText = "Weight:"
        val num = handleWeight(dialog.showAndWait()) ?: return null
        // on second selected vertex add edge
        val newEdge = Edge()
        newEdge.changeWeight(num)
        newEdge.addStart(selectedVertex!!)
        newEdge.addEnd(vertex)
        edgeArray.add(newEdge)
        selectedVertex!!.edges.add(newEdge)
        vertex.edges.add(newEdge)
        return newEdge
    }


    private fun drawGraph(){
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