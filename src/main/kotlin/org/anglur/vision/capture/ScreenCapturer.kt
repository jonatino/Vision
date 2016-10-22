package org.anglur.vision.capture

import java.awt.image.BufferedImage

abstract class ScreenCapturer : CaptureConfig() {
	
	abstract fun capture(): BufferedImage
	
	abstract fun destroy()
	
}