package org.anglur.vision.capture

import java.awt.GraphicsEnvironment
import java.awt.image.BufferedImage
import java.util.*
import java.util.concurrent.locks.ReentrantLock

abstract class ScreenCapturer {
	
	val NULL = BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
	
	val frameRate: Int = 30
	
	var showCursor = true
	
	var paused: Boolean = false
	
	val monitors by lazy {
		val list = ArrayList<Screen>()
		
		val defaultDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
		
		for (i in 0..GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices.lastIndex) {
			val device = GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices[i]
			if (device.iDstring == defaultDevice.iDstring)
				list.add(Screen(defaultDevice.defaultConfiguration.bounds, i, true))
			else
				list.add(Screen(device.defaultConfiguration.bounds, i, false))
		}
		
		list
	}
	
	private val lock = ReentrantLock()
	
	var currentScreen = 0
		set(value) {
			field = value
			val bounds = monitors[value].bounds
			lock.lock()
			try {
				resize(bounds.x, bounds.y, bounds.width, bounds.height)
			} finally {
				lock.unlock()
			}
		}
	
	var captureArea = monitors[currentScreen].bounds
		get() = monitors[currentScreen].bounds
	
	fun capture(): BufferedImage {
		if (paused) return NULL
		lock.lock()
		try {
			return snap()
		} finally {
			lock.unlock()
		}
	}
	
	protected abstract fun snap(): BufferedImage
	
	abstract fun resize(x: Int, y: Int, width: Int, height: Int)
	
	abstract fun destroy()
	
}