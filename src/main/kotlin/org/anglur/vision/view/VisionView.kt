package org.anglur.vision.view

import javafx.application.Platform.runLater
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import org.anglur.vision.util.AddressGrabber.address
import org.anglur.vision.util.IDSystem.encode
import org.anglur.vision.util.PassGenerator
import org.anglur.vision.util.splitEvery
import tornadofx.View
import kotlin.concurrent.thread

class VisionView : View() {

	override val root: AnchorPane by fxml()

	val connection: TextField by fxid()
	val id: Label by fxid()
	val password: Label by fxid()

	init {
		with(primaryStage) {
			title = "Vision"

			minHeight = 300.0
			minWidth = 575.0
			isResizable = false
			//TODO move CSS to external file once design is finalized (inline makes it easier in SceneBuilder)
			//stylesheets.add(Css.MAIN)
			//primaryStage.icons.add(Icons.FAVICON.image)

			runLater(connection::requestFocus)
		}

		password.text = PassGenerator.generatePass()

		thread {
			val address = address()
			runLater {
				id.text = encode(address).splitEvery(3)
			}
		}
	}

}