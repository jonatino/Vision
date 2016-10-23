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

package org.anglur.vision.capture

import java.awt.Rectangle

class Screen(val bounds: Rectangle, val id: Int, val isPrimary: Boolean = id == 0) {
	
	var active = isPrimary
	
	val orignalBounds = bounds.clone() as Rectangle
	
	val maxWidth = bounds.width
	
	val maxHeight = bounds.height
	
	override fun toString() = "Screen ${id + 1}"
}