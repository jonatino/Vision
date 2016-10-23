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

package org.anglur.vision.guid.generators.impl

import org.anglur.vision.guid.generators.Generator
import org.anglur.vision.net.http.IPFetcher.localAddress
import java.math.BigInteger
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer

class UIDGenerator : Generator {
	
	override fun generate(): String {
		val bytes = localAddress.address.address
		
		val buf = ByteBuffer.allocate(7)
		buf.put(1).putShort(localAddress.port.toShort()).put(bytes)
		
		return BigInteger(buf.array()).toString(36).toUpperCase().substring(1)
	}
	
	//TODO move this to proper location
	fun decode(id: String): InetSocketAddress {
		val buf = ByteBuffer.wrap(BigInteger("5" + id.toLowerCase(), 36).toByteArray())
		
		buf.get()
		val port = java.lang.Short.toUnsignedInt(buf.short)
		
		val address = ByteArray(4)
		buf.get(address)
		
		
		return InetSocketAddress(InetAddress.getByAddress(address), port)
	}
	
}