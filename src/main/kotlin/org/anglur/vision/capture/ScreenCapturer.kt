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
	
	val screens by lazy {
		val list = ArrayList<Screen>()
		
		val defaultDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
		list.add(Screen(defaultDevice.defaultConfiguration.bounds, 0, true))
		
		for (i in 1..GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices.lastIndex) {
			val device = GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices[i - 1]
			if (device.iDstring == defaultDevice.iDstring) continue
			
			list.add(Screen(device.defaultConfiguration.bounds, i, false))
		}
		
		list
	}
	
	private val lock = ReentrantLock()
	
	var currentScreen = 0
		set(value) {
			field = value
			val bounds = screens[value].bounds
			lock.lock()
			try {
				resize(bounds.x, bounds.y, bounds.width, bounds.height)
			} finally {
				lock.unlock()
			}
		}
	
	var captureArea = screens[currentScreen].bounds
		get() = screens[currentScreen].bounds
	
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