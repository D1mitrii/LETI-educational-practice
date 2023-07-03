package ru.etu.main

import javafx.event.EventHandler
import javafx.scene.shape.Circle
import javafx.scene.text.Text
import javafx.scene.text.TextBoundsType

class Vertex(name: String, x: Double, y: Double) : Circle() {

    var text: Text
    private val edges: MutableList<Edge> = mutableListOf()

    init {
        this.centerX = x
        this.centerY = y
        this.radius = 25.0
        this.id = "Vertex"
        this.text = Text(name)
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
        this.centerName()
    }

    private fun centerName(){
        val height = text.boundsInLocal.height
        val width = text.boundsInLocal.width
        println(" $height $width ")
        text.relocate(this.centerX - width/2, this.centerY - height/2)
    }

    fun changePosition(newX: Double, newY: Double) {
        this.centerX = newX
        this.centerY = newY
        centerName()
    }

}