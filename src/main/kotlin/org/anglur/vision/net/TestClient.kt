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
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.nio.NioDatagramChannel
import org.anglur.vision.util.extensions.writeString
import java.net.InetSocketAddress

object ClientTest {
	
	internal val PORT = 43594
	
	@JvmStatic fun main(args: Array<String>) {
		val group = NioEventLoopGroup()
		try {
			val b = Bootstrap()
			b.group(group)
					.channel(NioDatagramChannel::class.java)
					.handler(object : SimpleChannelInboundHandler<DatagramPacket>() {
						
						override fun channelRead0(ctx: ChannelHandlerContext, msg: DatagramPacket) {
							println("Read")
						}
						
					})
			
			val ch = b.bind(6345).sync().channel()
			
			val buff = Unpooled.buffer()
			buff.writeByte(0)
			buff.writeString("JJQ 5UZ 5CD")
			buff.writeString("password")
			ch.writeAndFlush(DatagramPacket(buff, InetSocketAddress("localhost", PORT))).sync()
			
			if (!ch.closeFuture().await(5000)) {
				System.err.println("Quote request timed out.")
			}
		} finally {
			group.shutdownGracefully()
		}
	}
	
}