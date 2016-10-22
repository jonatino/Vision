package org.anglur.vision.view

import org.anglur.vision.capture.CapturingMode
import org.anglur.vision.util.Scalr
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.SwingConstants

val background = JLabel("", SwingConstants.CENTER)

fun main(args: Array<String>) {
	val frame = JFrame("Desktop Capture")
	
	frame.setBounds(2552, 156, 1936, 1056)
	frame.contentPane.background = Color.BLACK
	frame.add(background, BorderLayout.CENTER)
	frame.isVisible = true
	
	val screengrabber = CapturingMode.JNA()
	
	screengrabber.width = 2560
	screengrabber.height = 1440
	
	object : Thread() {
		override fun run() {
			var iterations: Int = 0
			var time: Long = 0
			while (frame.isVisible) {
				val stamp = System.currentTimeMillis()
				val maxSize = Dimension(background.width, background.height)
				
				var image = screengrabber.capture()
				if (image.width > maxSize.width || image.height > maxSize.height)
					image = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.BEST_FIT_BOTH, maxSize.width, maxSize.height)
				
				background.icon = ImageIcon(image)
				time += System.currentTimeMillis() - stamp
				
				if (iterations++ % 100 == 0) {
					println("Took " + time / iterations.toDouble() + "ms avg per frame (over 100 frames)")
				}
			}
		}
	}.start()
}