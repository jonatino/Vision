package org.anglur.vision.net.http

import java.net.InetSocketAddress
import java.net.URL

object IPFetcher {
	
	val localAddress by lazy {
		val ip = URL("http://checkip.amazonaws.com/").readText().replace("\n", "")
		InetSocketAddress(ip, 65535)
	}
	
}
