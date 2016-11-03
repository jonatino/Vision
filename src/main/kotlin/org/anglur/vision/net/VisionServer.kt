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
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.util.AttributeKey
import org.anglur.vision.net.packet.incoming
import org.anglur.vision.util.extensions.readString

class UDPServer(val group: EventLoopGroup, val channel: Class<out Channel>) {
	
	val decoder = Decoder()
	
	fun bind(port: Int) = Bootstrap().group(group).handler(Handler(decoder)).channel(channel).bind(port)
	
}

internal class Handler(val decoder: Decoder) : ChannelInitializer<DatagramChannel>() {
	
	override fun initChannel(ch: DatagramChannel) {
		ch.pipeline().addFirst(decoder)
	}
	
}

inline fun udp(group: EventLoopGroup = NioEventLoopGroup(),
               channel: Class<out Channel> = NioDatagramChannel::class.java,
               init: UDPServer.() -> Unit): UDPServer {
	val server = UDPServer(group, channel)
	server.init()
	return server
}

class Decoder : MessageToMessageDecoder<DatagramPacket>() {
	
	override fun decode(ctx: ChannelHandlerContext, msg: DatagramPacket, out: MutableList<Any>) {
		val buff = msg.content()
		if (buff.readableBytes() > 0) {
			val packetId = buff.readUnsignedByte().toInt()
			println("Packet $packetId")
			
			//If secret has not been set, read the id instead. This should only happen during handshake (packet 0)
			val pos = buff.readerIndex()
			val session = Sessions[buff.readLong()] ?: Sessions[buff.readerIndex(pos).readString()]
			
			if (!ctx.channel().hasAttr(SESSION) && packetId != 0) {
				//If the key hasn't been set, than they are trying to fake a packet
				ctx.channel().close()
				return
			} else ctx.channel().attr(SESSION).set(session)
			
			incoming[packetId]!!(PacketPayload(buff, ctx, session))
		}
	}
	
	val SESSION = AttributeKey.valueOf<RemoteSession>("session")!!
	
	fun session(c: Channel): RemoteSession? = c.attr(SESSION).get()
	
	override fun channelActive(ctx: ChannelHandlerContext) {
		session(ctx.channel())?.connect()
	}
	
	override fun channelUnregistered(ctx: ChannelHandlerContext) {
		session(ctx.channel())?.disconnect()
	}
	
	override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) = TODO()
	
}

class PacketPayload(val buff: ByteBuf, val ctx: ChannelHandlerContext, val session: RemoteSession)

fun main(args: Array<String>) {
	udp {
		
	}.bind(43594).await()
}
