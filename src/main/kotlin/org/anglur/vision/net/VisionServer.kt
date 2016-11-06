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

package org.anglur.vision.net

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramChannel
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.util.AttributeKey

class UDPServer(val group: EventLoopGroup, val channel: Class<out Channel>) {
	
	val decoder = Decoder()
	val encoder = Encoder()
	
	fun bind(port: Int) = Bootstrap().group(group).handler(Handler(decoder, encoder)).channel(channel).bind(port)
	
}

val SESSION = AttributeKey.valueOf<RemoteSession>("session")!!

internal class Handler(val decoder: Decoder, val encoder: Encoder) : ChannelInitializer<DatagramChannel>() {
	
	override fun initChannel(ch: DatagramChannel) {
		ch.pipeline().addFirst(decoder, encoder)
	}
	
}

inline fun udp(group: EventLoopGroup = NioEventLoopGroup(),
               channel: Class<out Channel> = NioDatagramChannel::class.java,
               init: UDPServer.() -> Unit): UDPServer {
	val server = UDPServer(group, channel)
	server.init()
	return server
}

class PacketPayload(val buff: ByteBuf, val ctx: ChannelHandlerContext, val session: RemoteSession)

fun main(args: Array<String>) {
	udp {
		
	}.bind(43594).await()
}
