package org.anglur.vision.capture

import java.awt.Rectangle

abstract class CaptureConfig {
	
	val frameRate: Int = 30
	
	var showCursor = true
	
	var maxWidth = 2560
	var maxHeight = 1440
	
	var width = 2560
	
	var height = 1440
	
	var x = 0
	
	var y = 0
	
	var currentMonitor: Int = 1
	
	var paused: Boolean = false
	
	var monitors: Array<Rectangle> = Array(10) {
		Rectangle(2560, 1440)
	}
	
}