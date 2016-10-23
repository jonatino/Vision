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