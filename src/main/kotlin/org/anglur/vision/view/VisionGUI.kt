package org.anglur.vision.view

import javafx.application.Platform.runLater
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import org.anglur.vision.capture.CapturingMode
import org.anglur.vision.util.Clipboard
import org.anglur.vision.util.Password
import org.anglur.vision.util.UID
import tornadofx.View
import tornadofx.find
import kotlin.concurrent.thread

class VisionGUI : View() {
	
	override val root: AnchorPane by fxml()
	
	val connection: TextField by fxid()
	val id: Label by fxid()
	val password: Label by fxid()
	val copyToClipboard: Button by fxid()
	val generatePassword: Button by fxid()
	val connect: Button by fxid()
	
	val screengrabber = CapturingMode.JNA()
	
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
		
		connect.setOnAction {
			runLater {
				
				thread {
					val desktopFrame = find(DesktopFrame::class)
					val stage = Stage()
					stage.title = "Vision"
					stage.scene = Scene(desktopFrame.root, 1920.0, 1080.0)
					stage.show()
					
					var iterations: Int = 0
					var time: Long = 0
					while (stage.isShowing) {
						val stamp = System.currentTimeMillis()
						
						var image = screengrabber.capture()
						//if (image.width > maxSize.width || image.height > maxSize.height)
						//image = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.BEST_FIT_BOTH, maxSize.width, maxSize.height)
						desktopFrame.display(image)
						
						time += System.currentTimeMillis() - stamp
						
						if (iterations++ % 100 == 0) {
							println("Took " + time / iterations.toDouble() + "ms avg per frame (over 100 frames)")
						}
					}
				}
			}
			
		}
		thread {
			runLater {
				UID.create() //Run on new thread so we dont hang when trying to grab external ip :-)
			}
		}
	}
	
}