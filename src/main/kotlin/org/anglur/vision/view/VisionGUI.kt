/*
 *     Vision - free remote desktop software built with Kotlin
 *     Copyright (C) 2016  Jonathan Beaudoin
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.anglur.vision.view

import javafx.application.Platform.runLater
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import org.anglur.vision.capture.CaptureMode
import org.anglur.vision.guid.Password
import org.anglur.vision.guid.UID
import org.anglur.vision.util.Clipboard
import tornadofx.View
import kotlin.concurrent.thread

class VisionGUI : View() {
	
	override val root: AnchorPane by fxml()
	
	val connection: TextField by fxid()
	val id: Label by fxid()
	val password: Label by fxid()
	val copyToClipboard: Button by fxid()
	val generatePassword: Button by fxid()
	val connect: Button by fxid()
	
	companion object {
		
		val captureMode = CaptureMode.jna()
		
	}
	
	init {
		with(primaryStage) {
			title = "Vision"
			
			minHeight = 300.0
			minWidth = 575.0
			isResizable = false
			
			runLater {
				connection.requestFocus()
			}
		}
		
		password.textProperty().bind(Password.property)
		id.textProperty().bind(UID.property)
		
		generatePassword.setOnAction { Password.new() }
		copyToClipboard.setOnAction { Clipboard.set("vision:id=${id.text}:password=${password.text}") }
		
		connect.setOnAction {
			val desktopFrame = DesktopFrame.show()
			
			thread {
				var iterations: Int = 0
				var time: Long = 0
				while (desktopFrame.isShowing) {
					val stamp = System.currentTimeMillis()
					desktopFrame.display(captureMode.capture())
					time += System.currentTimeMillis() - stamp
					
					if (iterations++ % 100 == 0) {
						println("Took " + time / iterations.toDouble() + "ms avg per frame (over 100 frames)")
					}
					
					//Thread.sleep((1000.0 / captureMode.frameRate).toLong())
				}
			}
		}
		
		UID.create() //Run on new thread so we dont hang when trying to grab external ip :-)
	}
	
}