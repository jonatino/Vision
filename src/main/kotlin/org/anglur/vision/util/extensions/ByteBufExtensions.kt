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

package org.anglur.vision.util.extensions

import io.netty.buffer.ByteBuf
import org.anglur.vision.net.RemoteSession
import org.anglur.vision.net.packet.Packet

fun ByteBuf.readString(): String {
	val bldr = StringBuilder()
	
	var b: Byte
	while (readableBytes() > 0) {
		b = readByte()
		if (b.toInt() == 0) break
		
		bldr.append(b.toChar())
	}
	return bldr.toString()
}

fun ByteBuf.writeString(s: String) = this.writeBytes(s.toByteArray()).writeByte(0)


fun ByteBuf.writePacket(s: Packet, session: RemoteSession) = apply {
	clear()
	
	writeByte(s.id)
	writeLong(session.secret)
	
	s.invoke()
}