package org.anglur.vision.view

import javafx.application.Platform.runLater
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import org.anglur.vision.capture.CapturingMode
import org.anglur.vision.util.Clipboard
import org.anglur.vision.util.Password
import org.anglur.vision.util.UID
import tornadofx.View
import tornadofx.find
import java.net.InetAddress
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
			
			runLater(connection::requestFocus)
		}
		
		screengrabber.x = -1920
		screengrabber.y = 179
		screengrabber.width = 1920
		screengrabber.maxWidth = 1920
		screengrabber.height = 1080
		screengrabber.maxHeight = 1080
		
		password.textProperty().bind(Password.property())
		id.textProperty().bind(UID.property())
		
		generatePassword.setOnAction { Password.new() }
		copyToClipboard.setOnAction { Clipboard.set("vision:id=${id.text}:password=${password.text}") }
		
		val desktopFrame = find(DesktopFrame::class)
		val stage = Stage()
		stage.title = "Vision - Id: 432 340 439 Name: ${InetAddress.getLocalHost().hostName}"
		stage.icons.add(Image(VisionGUI::class.java.getResource("../../../../icon.png").toExternalForm()))
		stage.scene = Scene(desktopFrame.root, 1920.0, 1080.0, Color.BLACK)
		
		connect.setOnAction {
			stage.show()
			
			thread {
				var iterations: Int = 0
				var time: Long = 0
				while (stage.isShowing) {
					val stamp = System.currentTimeMillis()
					desktopFrame.display(screengrabber.capture())
					time += System.currentTimeMillis() - stamp
					
					if (iterations++ % 100 == 0) {
						println("Took " + time / iterations.toDouble() + "ms avg per frame (over 100 frames)")
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