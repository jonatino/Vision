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
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.image.Image
import org.anglur.vision.view.impl.ScalableImageView
import tornadofx.View
import tornadofx.hbox
import tornadofx.plusAssign
import java.awt.image.BufferedImage
import java.util.*

class DesktopFrame : View() {
	
	override val root: TabPane by fxml()
	
	val screenViews = ArrayList<ScalableImageView>()
	
	val captureConfig = VisionGUI.captureMode
	
	var currentView: ScalableImageView? = null
		get() = screenViews[captureConfig.currentScreen]
	
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
	
	
}