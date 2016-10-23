package org.anglur.vision.view

import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.embed.swing.SwingFXUtils
import javafx.scene.control.TabPane
import javafx.scene.image.Image
import tornadofx.View
import java.awt.image.BufferedImage

class DesktopFrame : View() {
	
	override val root: TabPane by fxml()
	
	val imageView: ScalableImageView by fxid()
	
	private val currentImage = SimpleObjectProperty<Image>()
	
	init {
		with(primaryStage) {
			imageView.imageProperty().bind(currentImage)
		}
	}
	
	fun display(img: BufferedImage) {
		imageView.maxWidth(img.width.toDouble())
		imageView.maxHeight(img.height.toDouble())
		
		Platform.runLater {
			currentImage.value = SwingFXUtils.toFXImage(img, null)
		}
	}
	
}