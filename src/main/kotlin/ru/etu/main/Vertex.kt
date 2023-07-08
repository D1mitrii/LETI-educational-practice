package ru.etu.main

import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.shape.Circle
import javafx.scene.text.Text
import javafx.scene.text.TextBoundsType

class Vertex(var name: String, x: Double, y: Double) : Circle(), Comparable<Vertex> {

    var text: Text
    val edges: MutableList<Edge> = mutableListOf()
    var minPath: Label = Label("")
    var isUsed: Boolean = false
    var currectPath: Int = Int.MAX_VALUE
        set(value) {
            field = value
            if (value == Int.MAX_VALUE)
            {
                minPath.text = "∞"
                return
            }
            minPath.text = value.toString()
        }

    init {
        this.centerX = x
        this.centerY = y
        this.radius = 25.0
        this.id = "Vertex"

        this.text = Text(name)
        this.text.id = "VertexName"
        this.text.boundsType = TextBoundsType.VISUAL
        this.text.onMouseClicked = EventHandler {
            it.consume()
        }
        this.text.onMouseEntered = EventHandler {
            this.isHover = true
        }
        this.text.onMouseExited = EventHandler {
            this.isHover = false
        }
        this.minPath.id = "MinPath"
        this.minPath.applyCss()
        this.centerName()
        this.setMinPath()
    }

    fun updateName(name: String) {
        this.name = name
        this.text.text = name
        this.centerName()
    }

    private fun centerName(){
        val height = text.boundsInLocal.height
        val width = text.boundsInLocal.width
        text.relocate(this.centerX - width/2, this.centerY - height/2)
    }

    private fun setMinPath(){
        val height = minPath.height
        val width = minPath.width
        var newPosX = centerX + radius * 0.8
        if (newPosX + width  >= WorkspaceWIDTH){
            newPosX = centerX - radius - width
        }
        minPath.relocate(newPosX, centerY - radius - height/3)
    }

    fun changePosition(newX: Double, newY: Double) {
        this.centerX = newX
        this.centerY = newY
        centerName()
        setMinPath()
        edges.forEach {
            it.moveEdge()
        }
    }

    override fun compareTo(other: Vertex): Int  = when{
        currectPath == other.currectPath -> name.compareTo(other.name)
        else -> currectPath - other.currectPath
    }

}