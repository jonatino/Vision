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
import javafx.beans.property.SimpleObjectProperty
import javafx.embed.swing.SwingFXUtils
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.stage.Stage
import org.anglur.vision.view.impl.ScalableImageView
import tornadofx.View
import tornadofx.find
import tornadofx.hbox
import tornadofx.plusAssign
import java.awt.image.BufferedImage
import java.net.InetAddress
import java.util.*

class DesktopFrame : View() {
	
	companion object {
		
		fun show(): DesktopFrame {
			val frame = find(DesktopFrame::class)
			frame.stage = Stage()
			frame.stage.title = "Vision - Id: 432 340 439 Name: ${InetAddress.getLocalHost().hostName}"
			frame.stage.icons.add(Image(VisionGUI::class.java.getResource("img/icon.png").toExternalForm()))
			frame.stage.scene = Scene(frame.root, 1920.0, 1080.0, Color.BLACK)
			frame.stage.show()
			return frame
		}
		
	}
	
	override val root: TabPane by fxml()
	
	val screenViews = ArrayList<ScalableImageView>()
	
	val captureConfig = VisionGUI.captureMode
	
	var currentView: ScalableImageView? = null
		get() = screenViews[captureConfig.currentScreen]
	
	lateinit var stage: Stage
	
	private val currentImage = SimpleObjectProperty<Image>()
	
	init {
		with(primaryStage) {
			for (i in 0..captureConfig.screens.lastIndex) {
				val screen = captureConfig.screens[i]
				val tab = Tab(screen.toString())
				tab.id = screen.id.toString()
				
				tab += hbox {
					alignment = Pos.CENTER
					style = "-fx-background-color: #191919;"
					
					with(ScalableImageView()) {
						screenViews += this
						this@hbox += this
					}
				}
				
				root.tabs += tab
			}
			currentView!!.imageProperty().bind(currentImage)
		}
		
		root.selectionModel.selectedItemProperty().addListener {
			ov, oldTab, newTab ->
			val old = oldTab.id.toInt()
			val new = newTab.id.toInt()
			
			screenViews[old].imageProperty().unbind()
			screenViews[new].imageProperty().bind(currentImage)
			
			captureConfig.currentScreen = new
		}
		
	}
	
	fun display(img: BufferedImage) {
		currentView!!.maxWidth(img.width.toDouble())
		currentView!!.maxHeight(img.height.toDouble())
		
		runLater {
			currentImage.value = SwingFXUtils.toFXImage(img, null)
		}
	}
	
	var isShowing: Boolean = false
		get() = stage.isShowing
	
	
}