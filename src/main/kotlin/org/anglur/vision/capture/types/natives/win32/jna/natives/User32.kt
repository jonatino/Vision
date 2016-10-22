package org.anglur.vision.capture.types.natives.win32.jna.natives

import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef

/**
 * Created by Jonathan on 10/21/2016.
 */
object User32 {
	
	val DI_COMPAT = 4
	val DI_DEFAULTSIZE = 8
	val DI_IMAGE = 2
	val DI_MASK = 1
	val DI_NORMAL = 3
	val DI_APPBANDING = 1
	
	external fun GetDesktopWindow(): WinDef.HWND
	
	external fun GetDC(hWnd: WinDef.HWND): WinDef.HDC
	
	external fun GetCursorInfo(pci: CURSORINFO): Boolean
	
	external fun DrawIconEx(hdc: WinDef.HDC, xLeft: Int,
	                        yTop: Int, hIcon: WinDef.HICON, cxWidth: Int, cyWidth: Int,
	                        istepIfAniCur: Int, hbrFlickerFreeDraw: WinDef.HBRUSH?,
	                        diFlags: Int): Boolean
	
	init {
		Native.register("user32")
	}
	
}