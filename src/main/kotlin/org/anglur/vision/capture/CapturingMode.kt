package org.anglur.vision.capture

import org.anglur.vision.capture.types.RobotCapture
import org.anglur.vision.capture.types.natives.win32.jna.GDICapture
import org.anglur.vision.capture.types.natives.win32.jna.JNACapture

object CapturingMode {
	
	fun JNA() = JNACapture()
	
	fun robot() = RobotCapture()
	
	fun FFMPEG() = GDICapture()
	
}