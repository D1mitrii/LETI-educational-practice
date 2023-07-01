package ru.etu.main

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.StageStyle

class MainApplication : Application() {

    override fun start(primStage: Stage) {
        val fxmlLoader = FXMLLoader(MainApplication::class.java.getResource("main.fxml"))
        primStage.scene = Scene(fxmlLoader.load(), 1280.0, 720.0)
        primStage.initStyle(StageStyle.UNDECORATED)
        primStage.isResizable = false
        primStage.show()
    }

}

fun main() {
    Application.launch(MainApplication::class.java)
}