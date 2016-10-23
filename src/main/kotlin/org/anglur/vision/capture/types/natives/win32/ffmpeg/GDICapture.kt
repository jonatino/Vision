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
	
	val grabber by lazy {
		val grabber = FFmpegFrameGrabber("desktop")
		grabber.format = "gdigrab"
		grabber.setOption("video_size", "${width}x$height")
		grabber.setOption("offset_x", "$x")
		grabber.setOption("offset_y", "$y")
		
		grabber
	}
	
	var running: Boolean = false
	
	fun start() {
		grabber.start()
	}
	
	override fun capture(): BufferedImage {
		if (!running) start()
		
		return converter.convert(grabber.grab())
	}
	
	override fun destroy() {
		grabber.stop()
	}
	
}