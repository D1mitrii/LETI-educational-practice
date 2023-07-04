package ru.etu.main

import javafx.scene.shape.Line

class Edge : Line() {

    private var weight: Double = 0.0
    var start: Vertex? = null
    var end: Vertex? = null

    init {
        this.id = "Edge"
    }

    fun changeWeight(weight: Double): Boolean{
        if (weight >= 0){
            this.weight = weight
            return true
        }
        return false
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
    }

    fun moveEdge(){
        this.startX = start!!.centerX
        this.startY = start!!.centerY

        this.endX = end!!.centerX
        this.endY = end!!.centerY
    }

}