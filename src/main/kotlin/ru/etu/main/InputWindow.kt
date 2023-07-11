package ru.etu.main

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.stage.FileChooser
import java.io.File
import java.lang.NumberFormatException


class InputWindow {


    @FXML
    private lateinit var TextArea: TextArea

    @FXML
    private lateinit var approveButton: Button

    @FXML
    private lateinit var openButton: Button

    private lateinit var graphController: GraphController

    private fun alertThrow(headerText : String, contentText : String)
    {
        val alert = Alert(AlertType.ERROR)

        alert.title = "Error alert"
        alert.headerText = headerText
        alert.contentText = contentText

        alert.showAndWait()
    }

    private fun checkLine(elems: Array<String>) : Boolean {
        if (elems.size == 1 && elems[0].isEmpty()) {
            alertThrow("Empty line", "Empty lines not allowed!")
            return false
        }
        if (elems.size != 3) {alertThrow("Incorrect line", "There should be 3 elements per line, except the first one"); return false}
        return true
    }

    fun setGraph(graphController: GraphController){
        this.graphController = graphController
    }

    @FXML
    fun initialize() {
        assert(TextArea != null) {"fx:id=\"TextArea\" was not injected: check your FXML file 'InputWndow.fxml'." }
        assert(approveButton != null) {"fx:id=\"approveButton\" was not injected: check your FXML file 'InputWndow.fxml'." }
        assert(openButton != null) {"fx:id=\"openButton\" was not injected: check your FXML file 'InputWindow.fxml'." }


        approveButton.onMousePressed = EventHandler {
            graphController.clear()
            val stringToVertex: MutableMap<String, Vertex> = mutableMapOf()
            val edges: MutableList<Edge> = mutableListOf()
            val Text = TextArea.text
            val N : Int
            val lines : List<String> = Text.lines()
            var x: Int; var y: Int
            var w: Int
            if (lines[0].isEmpty() || lines[0][0].isWhitespace())
            {
                alertThrow("Empty field!", "The field is empty!")

                return@EventHandler
            }

            try {
                N = lines[0].toInt()
            }
            catch (e: NumberFormatException){
                alertThrow("Wrong input!", "Digit must be first, not the letter/symbol!")
                return@EventHandler
            }

            for (i in 1 until  N + 1) {
                val elems = lines[i].split("\\s+".toRegex()).toTypedArray()
                if (!checkLine(elems)) return@EventHandler
                if (elems[0].length !in 1..2) {
                    alertThrow("Wrong name!", "The name of the vertex is incorrect!")
                    return@EventHandler
                }

                try {
                    x = elems[1].toInt()
                    y = elems[2].toInt()
                }
                catch (e: NumberFormatException){
                    alertThrow("Wrong coordinates", "Cords of Vertex X and Y must be integer")
                    return@EventHandler
                }

                if (x !in 0..980) {
                    alertThrow("Error X!", "The X coordinate is incorrect!")

                    return@EventHandler
                }
                if (y !in 0..624) {
                    alertThrow("Error Y!", "The Y coordinate is incorrect!")

                    return@EventHandler
                }
                stringToVertex[elems[0]] = Vertex(elems[0], x.toDouble(), y.toDouble())
            }

            for (i in N+1 until lines.size) {
                val elems = lines[i].split("\\s+".toRegex()).toTypedArray()
                if (!checkLine(elems)) return@EventHandler

                if (elems[0] == elems[1]) {
                    alertThrow("Loop error!", "There must be no loop in graph!")
                    return@EventHandler
                }
                if (elems[0].length !in 1..2 || elems[1].length !in 1..2 || stringToVertex[elems[0]] == null || stringToVertex[elems[1]] == null) {
                    alertThrow("Wrong name!", "The name of the vertex is incorrect!")
                    return@EventHandler
                }

                try {
                    w = elems[2].toInt()
                }
                catch (e: NumberFormatException){
                    alertThrow("Edge weight error!", "The weight of the edge must be integer!")

                    return@EventHandler
                }

                if (w !in 0..50) {
                    alertThrow("Edge weight error!", "The weight of the edge is incorrect!")

                    return@EventHandler
                }
                var edge: Edge? = null
                for (elem in edges){
                       if (elem.getVertex(stringToVertex[elems[0]]!!) == stringToVertex[elems[1]]){
                           edge = elem
                       }
                }
                if (edge != null){
                    edge.changeWeight(w)
                    continue
                }
                edge = Edge()
                edge.addStart(stringToVertex[elems[0]]!!)
                edge.addEnd(stringToVertex[elems[1]]!!)
                edge.changeWeight(w)
                edges.add(edge)
            }
            graphController.setGraph(stringToVertex.values.toMutableList(), edges)
            approveButton.scene.window.hide()
        }

        openButton.onMousePressed = EventHandler{
            val fileChooser = FileChooser()
            fileChooser.initialDirectory = File("./")
            val file = fileChooser.showOpenDialog(null)
            if (file != null)
            {
                TextArea.clear()
                for (line in file.readLines()) {
                    if (line.isNotEmpty())
                        TextArea.appendText(line + "\n")
                }
            }
        }
    }
}
