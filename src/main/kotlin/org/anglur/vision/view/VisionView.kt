package org.anglur.vision.view

import javafx.application.Platform.runLater
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import org.anglur.vision.util.Clipboard
import org.anglur.vision.util.Password
import org.anglur.vision.util.UID
import tornadofx.View
import kotlin.concurrent.thread

class VisionView : View() {

	override val root: AnchorPane by fxml()

	val connection: TextField by fxid()
	val id: Label by fxid()
	val password: Label by fxid()
	val copyToClipboard: Button by fxid()
	val generatePassword: Button by fxid()
	val connect: Button by fxid()

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
		
		password.textProperty().bind(Password.property())
		id.textProperty().bind(UID.property())
		
		generatePassword.setOnAction { Password.new() }
		copyToClipboard.setOnAction { Clipboard.set("vision:id=${id.text}:password=${password.text}") }
		
		thread {
			runLater {
				UID.create() //Run on new thread so we dont hang when trying to grab external ip :-)
			}
		}
	}

}