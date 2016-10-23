package org.anglur.vision.view

import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.embed.swing.SwingFXUtils
import javafx.scene.control.TabPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import tornadofx.View
import java.awt.image.BufferedImage

class DesktopFrame : View() {
	
	override val root: TabPane by fxml()
	
	val imageView: ImageView by fxid()
	
	private val currentImage = SimpleObjectProperty<Image>()
	
	init {
		with(primaryStage) {
			
			minHeight = 300.0
			minWidth = 575.0
			
			imageView.isPreserveRatio = true
			
			imageView.imageProperty().bind(currentImage)
			imageView.fitWidthProperty().bind(root.widthProperty())
			imageView.fitHeightProperty().bind(root.heightProperty())
		}
	}
	
	fun display(img: BufferedImage) {
		Platform.runLater {
			currentImage.value = SwingFXUtils.toFXImage(img, null)
		}
	}
	
}