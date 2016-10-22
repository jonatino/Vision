package org.anglur.vision.capture.types.natives.win32.jna.natives

import com.sun.jna.Structure
import com.sun.jna.platform.win32.WinDef

class CURSORINFO : Structure() {
	
	@JvmField var cbSize: Int = 0
	@JvmField var flags: Int = 0
	@JvmField var hCursor: WinDef.HCURSOR? = null
	@JvmField var ptScreenPos: WinDef.POINT? = null
	
	override fun getFieldOrder() = listOf("cbSize", "flags", "hCursor", "ptScreenPos")
	
}