package org.anglur.vision

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import tornadofx.App
import tornadofx.FX.Companion.application
import tornadofx.FX.Companion.primaryStage
import tornadofx.FX.Companion.stylesheets
import tornadofx.View
import tornadofx.find

fun main(args: Array<String>) {
	Application.launch(VisionApp::class.java)
}

class Vision : View() {
	
	override val root: AnchorPane by fxml()
	
	@FXML lateinit var connection: TextField
	
	init {
		with(primaryStage) {
			title = "Vision"
			
			minHeight = 300.0
			minWidth = 575.0
			isResizable = false
			title = "Vision"
			//TODO move CSS to external file once design is finalized (inline makes it easier in SceneBuilder)
			//stylesheets.add(Css.MAIN)
			//primaryStage.icons.add(Icons.FAVICON.image)
			
			Platform.runLater {
				connection.requestFocus()
			}
		}
	}
	
}

class VisionApp : App() {
	
	override val primaryView = Vision::class
	
	override fun start(stage: Stage) {
		primaryStage = stage
		application = this
		
		val view = find(primaryView)
		stage.apply {
			scene = Scene(view.root)
			scene.stylesheets.addAll(stylesheets)
			show()
		}
	}
	
}