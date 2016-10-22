package org.anglur.vision.capture.types.natives.win32.jna.natives

import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinGDI
import com.sun.jna.platform.win32.WinNT

/**
 * Created by Jonathan on 10/21/2016.
 */
object GDI32 {
	
	external fun BitBlt(hdcDest: WinDef.HDC, nXDest: Int, nYDest: Int, nWidth: Int, nHeight: Int, hdcSrc: WinDef.HDC, nXSrc: Int, nYSrc: Int, dwRop: Int): Boolean
	
	
	external fun GetDIBits(dc: WinDef.HDC, bmp: WinDef.HBITMAP, startScan: Int, scanLines: Int, pixels: ByteArray?, bi: WinGDI.BITMAPINFO, usage: Int): Boolean
	
	external fun GetDIBits(dc: WinDef.HDC, bmp: WinDef.HBITMAP, startScan: Int, scanLines: Int, pixels: ShortArray?, bi: WinGDI.BITMAPINFO, usage: Int): Boolean
	
	external fun GetDIBits(dc: WinDef.HDC, bmp: WinDef.HBITMAP, startScan: Int, scanLines: Int, pixels: IntArray?, bi: WinGDI.BITMAPINFO, usage: Int): Boolean
	
	external fun CreateCompatibleBitmap(hDC: WinDef.HDC, width: Int, height: Int): WinDef.HBITMAP
	
	external fun DeleteObject(hObject: WinNT.HANDLE): Boolean
	
	external fun SelectObject(hDC: WinDef.HDC, hGDIObj: WinNT.HANDLE): WinNT.HANDLE
	
	external fun CreateCompatibleDC(hDC: WinDef.HDC): WinDef.HDC
	
	var SRCCOPY = 0xCC0020
	
	init {
		Native.register("GDI32")
	}
	
}