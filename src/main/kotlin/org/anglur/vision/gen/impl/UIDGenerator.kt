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