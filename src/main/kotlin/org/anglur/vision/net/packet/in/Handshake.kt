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

package org.anglur.vision.net.packet.`in`

import org.anglur.vision.guid.Password
import org.anglur.vision.net.packet.incomingPacket
import org.anglur.vision.net.packet.out.HandshakeResponse
import org.anglur.vision.net.packet.out.HandshakeResponse.Response
import org.anglur.vision.util.extensions.readString

fun handshakePacket() = incomingPacket {
	val password = buff.readString()
	
	println("Username: ${session.id}, Password: $password")
	
	val denied = false //Place holder
	val timeout = false //Place holder
	
	var response = Response.ALLOW
	
	if (password != Password.get()) response = Response.INVALID_PASSWORD
	else if (denied) response = Response.DENY
	else if (timeout) response = Response.TIMEOUT
	
	if (response == Response.ALLOW) {
		session.ctx = ctx
	}
	
	session.write(HandshakeResponse(response))
}