package org.anglur.vision.capture.types

import org.anglur.vision.capture.ScreenCapturer
import sun.awt.ComponentFactory
import java.awt.GraphicsDevice
import java.awt.Rectangle
import java.awt.Toolkit
import java.awt.image.*
import java.awt.peer.RobotPeer
import java.lang.reflect.Method
import java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment as gfx

class RobotCapture constructor(device: GraphicsDevice = gfx().defaultScreenDevice) : ScreenCapturer() {
	
	private var getRGBPixelsMethodParam: Any? = null
	private var getRGBPixelsMethodType: Int = 0
	private var getRGBPixelsMethod: Method? = null
	private var peer: RobotPeer
	internal var model: ColorModel = DirectColorModel(32, 0xff0000, 0xff00, 0xff)
	
	init {
		val toolkit = Toolkit.getDefaultToolkit()
		peer = (toolkit as ComponentFactory).createRobot(null, device)
		val peerClass = peer.javaClass
		var method: Method? = null
		var methodType = -1
		var methodParam: Any? = null
		try {
			method = peerClass.getDeclaredMethod("getRGBPixels", *arrayOf<Class<*>>(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, IntArray::class.java))
			methodType = 0
		} catch (ex: Exception) {
		}
		if (methodType < 0) {
			try {
				method = peerClass.getDeclaredMethod("getScreenPixels", *arrayOf<Class<*>>(Rectangle::class.java, IntArray::class.java))
				methodType = 1
			} catch (ex: Exception) {
			}
		}
		if (methodType < 0) {
			try {
				method = peerClass.getDeclaredMethod("getScreenPixels", *arrayOf<Class<*>>(Integer.TYPE, Rectangle::class.java, IntArray::class.java))
				methodType = 2
				val devices = gfx().screenDevices
				val count = devices.size
				for (i in 0..count - 1)
					if (device == devices[i]) {
						methodParam = Integer.valueOf(i)
						break
					}
			} catch (ex: Exception) {
			}
		}
		if (methodType < 0) {
			try {
				method = peerClass.getDeclaredMethod("getRGBPixelsImpl", *arrayOf<Class<*>>(Class.forName("sun.awt.X11GraphicsConfig"), Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, IntArray::class.java))
				methodType = 3
				val field = peerClass.getDeclaredField("xgc")
				try {
					field.isAccessible = true
					methodParam = field.get(peer)
				} finally {
					field.isAccessible = false
				}
			} catch (ex: Exception) {
			}
		}
		if (methodType >= 0) {
			getRGBPixelsMethod = method
			getRGBPixelsMethodType = methodType
			getRGBPixelsMethodParam = methodParam
		}
	}
	
	fun getRGBPixel(x: Int, y: Int) = peer.getRGBPixel(x, y)
	
	fun getRGBPixels(bounds: Rectangle) = peer.getRGBPixels(bounds)
	
	fun getRGBPixels(x: Int, y: Int, width: Int, height: Int, pixels: IntArray): Boolean {
		if (getRGBPixelsMethod != null) {
			when (getRGBPixelsMethodType) {
				0 -> getRGBPixelsMethod!!.invoke(peer, arrayOf<Any>(Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(width), Integer.valueOf(height), pixels))
				1 -> getRGBPixelsMethod!!.invoke(peer, arrayOf(Rectangle(x, y, width, height), pixels))
				2 -> getRGBPixelsMethod!!.invoke(peer, arrayOf(getRGBPixelsMethodParam!!, Rectangle(x, y, width, height), pixels))
				else -> getRGBPixelsMethod!!.invoke(peer, arrayOf<Any>(getRGBPixelsMethodParam!!, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(width), Integer.valueOf(height), pixels))
			}
			return true
		}
		val tmp = getRGBPixels(Rectangle(x, y, width, height))
		System.arraycopy(tmp, 0, pixels, 0, width * height)
		return false
	}
	
	override fun resize(x: Int, y: Int, width: Int, height: Int) {
	}
	
	override fun destroy() {
		getRGBPixelsMethod?.isAccessible = false
		peer.javaClass.getDeclaredMethod("dispose", null).invoke(peer, arrayOfNulls<Class<*>>(0))
	}
	
	override fun snap() =
			BufferedImage(model, Raster.createWritableRaster(model.createCompatibleSampleModel(captureArea.width, captureArea.height),
					DataBufferInt(getRGBPixels(Rectangle(captureArea.width, captureArea.height)), captureArea.width * captureArea.height), null), false, null)
	
}