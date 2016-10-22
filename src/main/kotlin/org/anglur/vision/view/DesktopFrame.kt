package org.anglur.vision.view

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
			title = "Vision"
			
			minHeight = 300.0
			minWidth = 575.0
			//TODO move CSS to external file once design is finalized (inline makes it easier in SceneBuilder)
			//stylesheets.add(Css.MAIN)
			//primaryStage.icons.add(Icons.FAVICON.image)
		}
		
		imageView.imageProperty().bind(currentImage)
		imageView.image
	}
	
	fun display(img: BufferedImage) {
		currentImage.value = SwingFXUtils.toFXImage(img, null)
	}
	
}