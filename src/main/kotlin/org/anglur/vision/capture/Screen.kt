package org.anglur.vision.capture

import java.awt.Rectangle

class Screen(val bounds: Rectangle, val id: Int, val isPrimary: Boolean = id == 0) {
	
	var active = isPrimary
	
	val orignalBounds = bounds.clone() as Rectangle
	
	val maxWidth = bounds.width
	
	val maxHeight = bounds.height
	
	override fun toString() = "Screen ${id + 1}"
}