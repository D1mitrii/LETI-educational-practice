package ru.etu.main

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Vertex(var name: String, val x: Double, val y: Double) : Circle() {
    val edges: MutableList<Edge> = mutableListOf()

    init {
        this.centerX = x
        this.centerY = y
        this.fill = Color.RED
        this.radius = 25.0
    }
}