package org.anglur.vision

import javafx.application.Application
import javafx.application.Platform
import javafx.application.Platform.runLater
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
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

	val connection: TextField by fxid()
	val id: Label by fxid()
	val password: Label by fxid()

	init {
		id.text = encode(address()).splitEvery(3)
		password.text = generatePass().toString()


		with(primaryStage) {
			title = "Vision"

			minHeight = 300.0
			minWidth = 575.0
			isResizable = false
			title = "Vision"
			//TODO move CSS to external file once design is finalized (inline makes it easier in SceneBuilder)
			//stylesheets.add(Css.MAIN)
			//primaryStage.icons.add(Icons.FAVICON.image)

			runLater(connection::requestFocus)
		}
	}

}

fun String.splitEvery(n: Int) = split(Regex("(?<=\\G${"." * n})")).joinToString(" ")

operator fun String.times(n: Int): String {
	var result = this
	if (n > 1) for (i in 1..n - 1) result += this
	return result
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