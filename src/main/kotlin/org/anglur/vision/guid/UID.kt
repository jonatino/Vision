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
import org.anglur.vision.util.extensions.fxThread
import org.anglur.vision.util.extensions.splitEvery
import java.math.BigInteger
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer

object UID {
	
	val generator = UIDGenerator()
	
	val property: SimpleStringProperty by lazy {
		val prop = SimpleStringProperty()
		
		fxThread {
			prop.set(generator.generate().splitEvery(3))
		}
		
		prop
	}
	
	fun get() = property.get()
	
	
	//TODO move this to proper location
	fun raw(): InetSocketAddress {
		val id = "5${property.get().replace(" ", "").toLowerCase()}"
		val buf = ByteBuffer.wrap(BigInteger(id, 36).toByteArray())
		
		buf.get()
		val port = java.lang.Short.toUnsignedInt(buf.short)
		
		val address = ByteArray(4)
		buf.get(address)
		
		
		return InetSocketAddress(InetAddress.getByAddress(address), port)
	}
}