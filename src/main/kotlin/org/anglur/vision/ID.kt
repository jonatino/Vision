package org.anglur.vision

import java.lang.Short.toUnsignedInt
import java.math.BigInteger
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.URL
import java.nio.ByteBuffer

fun main(args: Array<String>) {
	val address = address()
	println("Address: $address")
	val encode = encode(address)
	println("Encode: $encode")
	val decode = decode(encode)
	println("Decode: $decode")
}

fun address(): InetSocketAddress {
	val ip = URL("http://checkip.amazonaws.com/").readText().trim().replace("\n", "")
	return InetSocketAddress(ip, Short.MAX_VALUE.toInt())
}

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