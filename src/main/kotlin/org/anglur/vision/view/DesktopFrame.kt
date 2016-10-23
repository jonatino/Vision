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
import tornadofx.add
import tornadofx.hbox
import tornadofx.plusAssign
import java.awt.image.BufferedImage
import java.util.*

class DesktopFrame : View() {
	
	override val root: TabPane by fxml()
	
	val screenViews = ArrayList<ScalableImageView>()
	
	val captureConfig = VisionGUI.screengrabber
	
	var currentView: ScalableImageView? = null
		get() = screenViews[captureConfig.currentScreen]
	
	private val currentImage = SimpleObjectProperty<Image>()
	
	init {
		with(primaryStage) {
			for (i in 0..captureConfig.monitors.lastIndex) {
				val screen = captureConfig.monitors[i]
				val tab = Tab(screen.toString())
				tab.id = screen.id.toString()
				
				this += hbox {
					alignment = Pos.CENTER
					style = "-fx-background-color: #191919;"
					
					val imageView = ScalableImageView()
					screenViews.add(imageView)
					add(imageView)
					
					tab.add(this)
					root.tabs.add(tab)
				}
			}
			currentView!!.imageProperty().bind(currentImage)
		}
		
		root.selectionModel.selectedItemProperty().addListener { ov, oldTab, newTab ->
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