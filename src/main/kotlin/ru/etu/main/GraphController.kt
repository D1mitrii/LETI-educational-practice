package ru.etu.main

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Cursor
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.layout.Pane
import java.net.URL
import java.util.*

class GraphController(var pane: Pane){

    @FXML
    private var renameOption: MenuItem = MenuItem("Rename")

    @FXML
    private var deleteOption: MenuItem = MenuItem("Delete")


    @FXML
    private var contextMenu: ContextMenu = ContextMenu(renameOption, deleteOption)

    val startArea: Pair<Double, Double> = Pair<Double, Double>(pane.layoutX, pane.layoutY)
    val width: Double = 980.0
    val height: Double = 624.0

    private val vertexArray: MutableList<Vertex> = mutableListOf()

    fun createVertex(x: Double, y: Double) : Vertex {
        val vertex = Vertex("A", x - startArea.first, y - startArea.second)
        vertex.onMouseEntered = EventHandler {
            vertex.cursor = Cursor.HAND
        }
        vertex.onMouseDragged = EventHandler {
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
            vertex.centerX = newX
            vertex.centerY = newY
        }
        vertex.onMouseClicked = EventHandler {
            it.consume()
            if (it.isStillSincePress) {
                return@EventHandler
            }
            if (!it.isSecondaryButtonDown){
                return@EventHandler
            }
            contextMenu.show(pane, it.screenX, it.screenY)
            renameOption.onAction = EventHandler {
                if (contextMenu.isShowing) {
                    contextMenu.hide()
                }
            }
            deleteOption.onAction = EventHandler {
                if (contextMenu.isShowing) {
                    contextMenu.hide()
                }
            }
        }
        vertexArray.add(vertex)
        return vertex
    }

}