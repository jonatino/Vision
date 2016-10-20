package org.anglur.vision.util

import java.net.InetSocketAddress
import java.net.URL

object AddressGrabber {

	fun address(): InetSocketAddress {
		val ip = URL("http://checkip.amazonaws.com/").readText().trim().replace("\n", "")
		return InetSocketAddress(ip, 65535)
	}

}