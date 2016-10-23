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

package org.anglur.vision.view.impl

import javafx.scene.image.ImageView

class ScalableImageView : ImageView() {
	
	init {
		isPreserveRatio = true
	}
	
	private var maxWidth = -1.0
	private var maxHeight = -1.0
	
	override fun minWidth(width: Double) = maxWidth(width) / 2
	
	override fun prefWidth(width: Double): Double {
		val I = image ?: return maxWidth(width)
		return I.width
	}
	
	override fun maxWidth(width: Double): Double {
		if (maxWidth == -1.0 && width > 0)
			maxWidth = width
		return maxWidth
	}
	
	override fun minHeight(height: Double) = maxHeight(height) / 2
	
	override fun prefHeight(height: Double): Double {
		val I = image ?: return maxHeight(height)
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