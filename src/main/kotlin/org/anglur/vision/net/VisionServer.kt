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
import org.anglur.vision.guid.Password
import org.anglur.vision.net.packet.out.Response
import org.anglur.vision.net.packet.out.handshakeResponse
import org.anglur.vision.util.extensions.readString
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class UDPServer(val group: EventLoopGroup, val channel: Class<out Channel>) {
	
	val decoder = Decoder()
	
	fun bind(port: Int) = Bootstrap().group(group).handler(Handler(decoder)).channel(channel).bind(port)
	
	inner class IncomingPackets {
		internal operator fun Int.invoke(body: ByteBuf.() -> Unit) {
			decoder.handlers.put(this, body)
		}
	}
	
	internal inline fun incoming(body: IncomingPackets.() -> Unit) {
		IncomingPackets().body()
	}
	
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
			
			val theirId = buff.readString()
			val session = Sessions[theirId]
			
			println("Packet $packetId")
			if (!ctx.channel().hasAttr(SESSION) && packetId != 0) {
				//If the key hasn't been set, than they are trying to fake a packet
				ctx.channel().close()
				return
			}
			when (packetId) {
				0 -> {
					val password = buff.readString()
					val computerName = buff.readString()
					
					println("Password: $password, ComputerName: $computerName")
					
					val denied = false //Place holder
					val timeout = false //Place holder
					
					var response = Response.ALLOW
					
					if (password != Password.get()) response = Response.INVALID_PASSWORD
					else if (denied) response = Response.DENY
					else if (timeout) response = Response.TIMEOUT
					
					var secret = -1L
					if (response == Response.ALLOW) {
						secret = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE)
						session.secret = secret
						session.channel = ctx.channel()
						ctx.channel().attr(SESSION).set(session)
					}
					handshakeResponse(response, secret)
				}
				
				
				else -> handlers[packetId]!!(buff)
			}
		}
	}
	
	val SESSION = AttributeKey.valueOf<RemoteSession>("session")!!
	
	val handlers = HashMap<Int, ByteBuf.() -> Unit>()
	
	fun session(c: Channel): RemoteSession {
		return c.attr(SESSION).get()!!
	}
	
	override fun channelActive(ctx: ChannelHandlerContext) {
		session(ctx.channel()).connect()
	}
	
	override fun channelUnregistered(ctx: ChannelHandlerContext) {
		session(ctx.channel()).disconnect()
	}
	
	override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
	}
	
	
}

fun main(args: Array<String>) {
	udp {
	}.bind(43594).await()
}
