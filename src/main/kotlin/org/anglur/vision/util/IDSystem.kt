package org.anglur.vision.util

import java.lang.Short.toUnsignedInt
import java.math.BigInteger
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer

object IDSystem {

	fun encode(address: InetSocketAddress): String {
		val bytes = address.address.address

		val buf = ByteBuffer.allocate(6)
		buf.put(bytes).putShort(address.port.toShort())

		return BigInteger(buf.array()).toString(36).toUpperCase()
	}

	fun decode(id: String): InetSocketAddress {
		val buf = ByteBuffer.wrap(BigInteger(id.toLowerCase(), 36).toByteArray())

		val address = ByteArray(4)
		buf.get(address)

		val port = toUnsignedInt(buf.short)

		return InetSocketAddress(InetAddress.getByAddress(address), port)
	}

}