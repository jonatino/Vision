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

package org.anglur.vision.capture.types.natives.win32.jna

import org.anglur.vision.capture.ScreenCapturer
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import java.awt.image.BufferedImage

/**
 * Created by Jonathan on 10/21/2016.
 */
class GDICapture : ScreenCapturer() {
	
	val converter = Java2DFrameConverter()
	
	var grabber = FFmpegFrameGrabber("desktop")
	
	var running: Boolean = false
	
	fun start() {
		grabber.format = "gdigrab"
		grabber.start()
	}
	
	override fun snap(): BufferedImage {
		if (!running) start()
		
		return converter.convert(grabber.grab())
	}
	
	override fun resize(x: Int, y: Int, width: Int, height: Int) {
		grabber.setOption("video_size", "${width}x$height")
		grabber.setOption("offset_x", "$y")
		grabber.setOption("offset_y", "$x")
	}
	
	override fun destroy() {
		grabber.stop()
	}
	
}