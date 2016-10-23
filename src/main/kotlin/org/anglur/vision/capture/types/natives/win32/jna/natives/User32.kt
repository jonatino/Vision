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