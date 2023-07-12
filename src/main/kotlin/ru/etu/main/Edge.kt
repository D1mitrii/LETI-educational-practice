package ru.etu.main

import javafx.scene.shape.Line
import javafx.scene.text.Text

class Edge : Line() {

    var weight: Int = 0
        set(value) {
            field = value
            this.weightText.text = value.toString()
        }
    var start: Vertex? = null
    var end: Vertex? = null
    var weightText: Text = Text("$weight")

    init {
        this.id = "Edge"
        weightText.id = "EdgeWeight"
    }

    fun changeWeight(weight: Int): Boolean{
        if (weight >= 0){
            this.weight = weight
            return true
        }
        return false
    }

    private fun getMiddle(): Pair<Double, Double>{
        return Pair((startX + endX) / 2, (startY + endY) / 2)
    }

    fun addStart(vertex: Vertex){
        start = vertex
        this.startX = vertex.centerX
        this.startY = vertex.centerY
        start!!.edges.add(this)
        relocateWeight()
    }

    fun addEnd(vertex: Vertex){
        end = vertex
        this.endX = vertex.centerX
        this.endY = vertex.centerY
        end!!.edges.add(this)
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

    fun getVertex(vertex: Vertex) : Vertex {
        if (start == vertex) return end!!
        return start!!
    }

}