package org.anglur.vision.gen.impl

import org.anglur.vision.gen.Generator
import org.anglur.vision.net.http.IPFetcher.localAddress
import java.math.BigInteger
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer

class UIDGenerator : Generator() {
	
	override fun generate(): String {
		val bytes = localAddress.address.address
		
		val buf = ByteBuffer.allocate(6)
		buf.put(bytes).putShort(localAddress.port.toShort())
		
		return BigInteger(buf.array()).toString(36).toUpperCase()
	}
	
	//TODO move this to proper location
	fun decode(id: String): InetSocketAddress {
		val buf = ByteBuffer.wrap(BigInteger(id.toLowerCase(), 36).toByteArray())
		
		val address = ByteArray(4)
		buf.get(address)
		
		val port = java.lang.Short.toUnsignedInt(buf.short)
		
		return InetSocketAddress(InetAddress.getByAddress(address), port)
	}
	
}