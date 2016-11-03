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
import javafx.scene.effect.DropShadowBuilder
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import org.anglur.vision.capture.CaptureMode
import org.anglur.vision.guid.Password
import org.anglur.vision.guid.UID
import org.anglur.vision.net.RemoteSession
import org.anglur.vision.util.Clipboard
import org.anglur.vision.util.extensions.withMultiple
import tornadofx.View
import java.awt.Desktop
import java.net.URI

class VisionGUI : View() {
	
	override val root: AnchorPane by fxml()
	
	val connection: TextField by fxid()
	val id: Label by fxid()
	val password: Label by fxid()
	val copyToClipboard: Button by fxid()
	val generatePassword: Button by fxid()
	val connect: Button by fxid()
	val github: ImageView by fxid()
	val githubLink: Label by fxid()
	
	companion object {
		
		val captureMode by lazy { CaptureMode.jna() }
	}
	
	val mouseOver = DropShadowBuilder.create()
			.color(Color.rgb(50, 50, 50, .588)).build()!!
	
	init {
		with(primaryStage) {
			title = "Vision"
			
			isResizable = false
			
			runLater {
				connection.requestFocus()
			}
		}
		
		password.textProperty().bind(Password.property)
		id.textProperty().bind(UID.property)
		
		generatePassword.setOnAction { Password.new() }
		copyToClipboard.setOnAction { Clipboard.set("vision:id=${id.text}:password=${password.text}") }
		
		withMultiple(githubLink, github) {
			
			setOnMouseExited {
				githubLink.isUnderline = false
				github.effect = null
			}
			
			setOnMouseEntered {
				githubLink.isUnderline = true
				github.effect = mouseOver
			}
			
			setOnMouseClicked {
				if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(URI("https://github.com/Jonatino/Vision"))
			}
			
		}
		
		connect.setOnAction {
			val input = connection.text.replace("vision:id=", "").split(":password=")
			val id = UID.get() //input[0]
			val password = Password.get() //input[1]
			
			val session = RemoteSession(id, password)
			session.connect()
		}
		
		UID.get()
	}
	
}