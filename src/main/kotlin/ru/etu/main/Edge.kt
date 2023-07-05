package ru.etu.main

import javafx.scene.shape.Line
import javafx.scene.text.Text

class Edge : Line() {

    private var weight: Double = 0.0
        set(value) {
            field = value
            this.weightText.text = value.toString()
        }
    var start: Vertex? = null
    var end: Vertex? = null
    var weightText: Text = Text("0")

    init {
        this.id = "Edge"
        weightText.id = "VertexName"
    }

    fun changeWeight(weight: Double): Boolean{
        if (weight >= 0){
            this.weight = weight
            return true
        }
        return false
    }

    private fun getMiddle(): Pair<Double, Double>{
        return Pair<Double, Double>((startX + endX) / 2, (startY + endY) / 2)
    }

    fun addStart(vertex: Vertex){
        start = vertex
        this.startX = vertex.centerX
        this.startY = vertex.centerY
    }

    fun addEnd(vertex: Vertex){
        end = vertex
        this.endX = vertex.centerX
        this.endY = vertex.centerY
        relocateWeight()
    }

    private fun relocateWeight(){
        val height = weightText.boundsInLocal.height
        val width = weightText.boundsInLocal.width
        val middle = getMiddle()
        weightText.relocate(middle.first - width / 2, middle.second - height / 2)
    }

    fun moveEdge(){
        this.startX = start!!.centerX
        this.startY = start!!.centerY

        this.endX = end!!.centerX
        this.endY = end!!.centerY
        relocateWeight()
    }

}