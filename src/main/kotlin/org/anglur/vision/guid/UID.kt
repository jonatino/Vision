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

package org.anglur.vision.guid

import javafx.beans.property.SimpleStringProperty
import org.anglur.vision.guid.generators.impl.UIDGenerator
import org.anglur.vision.util.extensions.splitEvery

object UID {
	
	val generator = UIDGenerator()
	
	val property by lazy {
		SimpleStringProperty(generator.generate().splitEvery(3))
	}
	
	fun create() = property.get()
	
}