package org.anglur.vision.view

import javafx.scene.image.ImageView

class ScalableImageView : ImageView() {
	
	init {
		isPreserveRatio = true
	}
	
	private var maxWidth = -1.0
	private var maxHeight = -1.0
	
	override fun minWidth(width: Double) = 300.0
	
	override fun prefWidth(width: Double): Double {
		val I = image ?: return minWidth(width)
		return I.width
	}
	
	override fun maxWidth(width: Double): Double {
		if (maxWidth == -1.0 && width > 0)
			maxWidth = width
		return maxWidth
	}
	
	override fun minHeight(height: Double) = 300.0
	
	override fun prefHeight(height: Double): Double {
		val I = image ?: return minHeight(height)
		return I.height
	}
	
	override fun maxHeight(height: Double): Double {
		if (maxHeight == -1.0 && height > 0)
			maxHeight = height
		return maxHeight
	}
	
	override fun isResizable() = true
	
	override fun resize(width: Double, height: Double) {
		fitWidth = width
		fitHeight = height
	}
	
}