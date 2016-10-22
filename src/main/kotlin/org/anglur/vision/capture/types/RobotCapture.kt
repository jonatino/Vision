package org.anglur.vision.capture.types

import org.anglur.vision.capture.ScreenCapturer
import sun.awt.ComponentFactory
import java.awt.*
import java.awt.image.*
import java.awt.peer.MouseInfoPeer
import java.awt.peer.RobotPeer
import java.lang.reflect.Method
import java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment as gfx

class RobotCapture constructor(device: GraphicsDevice = gfx().defaultScreenDevice) : ScreenCapturer() {
	
	private var getRGBPixelsMethodParam: Any? = null
	private var getRGBPixelsMethodType: Int = 0
	private var getRGBPixelsMethod: Method? = null
	private var peer: RobotPeer
	internal var model: ColorModel = DirectColorModel(32, 0xff0000, 0xff00, 0xff)
	private var hasMouseInfoPeer: Boolean = false
	private var mouseInfoPeer: MouseInfoPeer? = null
	
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
		if (methodType < 0)
			try {
				method = peerClass.getDeclaredMethod("getScreenPixels", *arrayOf<Class<*>>(Rectangle::class.java, IntArray::class.java))
				methodType = 1
			} catch (ex: Exception) {
			}
		if (methodType < 0)
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
		if (methodType < 0)
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
		if (methodType >= 0) {
			getRGBPixelsMethod = method
			getRGBPixelsMethodType = methodType
			getRGBPixelsMethodParam = methodParam
		}
	}
	
	fun getRGBPixel(x: Int, y: Int) = peer.getRGBPixel(x, y)
	
	fun getRGBPixels(bounds: Rectangle) = peer.getRGBPixels(bounds)
	
	fun getRGBPixels(x: Int, y: Int, width: Int, height: Int, pixels: IntArray): Boolean {
		if (getRGBPixelsMethod != null)
			try {
				if (getRGBPixelsMethodType == 0)
					getRGBPixelsMethod!!.invoke(peer, arrayOf<Any>(Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(width), Integer.valueOf(height), pixels))
				else if (getRGBPixelsMethodType == 1)
					getRGBPixelsMethod!!.invoke(peer, arrayOf(Rectangle(x, y, width, height), pixels))
				else if (getRGBPixelsMethodType == 2)
					getRGBPixelsMethod!!.invoke(peer, arrayOf(getRGBPixelsMethodParam!!, Rectangle(x, y, width, height), pixels))
				else
					getRGBPixelsMethod!!.invoke(peer, arrayOf<Any>(getRGBPixelsMethodParam!!, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(width), Integer.valueOf(height), pixels))
				return true
			} catch (ex: Exception) {
			}
		val tmp = getRGBPixels(Rectangle(x, y, width, height))
		System.arraycopy(tmp, 0, pixels, 0, width * height)
		return false
	}
	
	override fun destroy() {
		getRGBPixelsMethodParam = null
		val method = getRGBPixelsMethod
		if (method != null) {
			getRGBPixelsMethod = null
			try {
				method.isAccessible = false
			} catch (ex: Exception) {
			}
		}
		//Using reflection now because of some peers not having ANY support at all (1.5)
		try {
			peer.javaClass.getDeclaredMethod("dispose", null).invoke(peer, arrayOfNulls<Class<*>>(0))
		} catch (ex: Exception) {
		}
	}
	
	override fun capture() =
			BufferedImage(model, Raster.createWritableRaster(model.createCompatibleSampleModel(width, height),
					DataBufferInt(getRGBPixels(Rectangle(width, height)), width * height), null), false, null)
	
	fun getMouseInfo(point: Point?): GraphicsDevice {
		if (!hasMouseInfoPeer) {
			hasMouseInfoPeer = true
			try {
				val toolkit = Toolkit.getDefaultToolkit()
				val method = toolkit.javaClass.getDeclaredMethod("getMouseInfoPeer", null)
				try {
					method.isAccessible = true
					mouseInfoPeer = method.invoke(toolkit, arrayOfNulls<Any>(0)) as MouseInfoPeer
				} finally {
					method.isAccessible = false
				}
			} catch (ex: Exception) {
			}
		}
		if (mouseInfoPeer != null) {
			val device = mouseInfoPeer!!.fillPointWithCoords(point ?: Point())
			val devices = GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices
			return devices[device]
		}
		val info = MouseInfo.getPointerInfo()
		if (point != null) {
			val location = info.location
			point.x = location.x
			point.y = location.y
		}
		return info.device
	}
	
	val screenDevice = getMouseInfo(null)
	
}