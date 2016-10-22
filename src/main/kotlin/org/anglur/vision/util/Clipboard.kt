package org.anglur.vision.util

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

object Clipboard {
	
	fun set(s: String) {
		val s = StringSelection(s)
		Toolkit.getDefaultToolkit().systemClipboard.setContents(s, s)
	}
	
}