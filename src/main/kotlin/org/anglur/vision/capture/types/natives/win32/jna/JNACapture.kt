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

package org.anglur.vision.capture.types.natives.win32.jna

import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinGDI
import com.sun.jna.platform.win32.WinGDI.DIB_RGB_COLORS
import org.anglur.vision.capture.ScreenCapturer
import org.anglur.vision.capture.types.natives.win32.jna.natives.CURSORINFO
import org.anglur.vision.capture.types.natives.win32.jna.natives.GDI32
import org.anglur.vision.capture.types.natives.win32.jna.natives.User32
import org.anglur.vision.capture.types.natives.win32.jna.natives.User32.DI_NORMAL
import java.awt.image.*

/**
 * Created by Jonathan on 10/21/2016.
 */
class JNACapture : ScreenCapturer() {
	
	private var windowDC = User32.GetDC(User32.GetDesktopWindow())
	private var blitDC = GDI32.CreateCompatibleDC(windowDC)
	private var outputBitmap: WinDef.HBITMAP
	
	init {
		outputBitmap = GDI32.CreateCompatibleBitmap(windowDC, captureArea.width, captureArea.height)
		GDI32.SelectObject(blitDC, outputBitmap)
	}
	
	override fun snap(): BufferedImage {
		GDI32.BitBlt(blitDC, 0, 0, captureArea.width, captureArea.height, windowDC, captureArea.x, captureArea.y, GDI32.SRCCOPY)
		
		if (showCursor) {
			val ci = CURSORINFO()
			ci.cbSize = ci.size()
			if (User32.GetCursorInfo(ci))
				User32.DrawIconEx(blitDC, ci.ptScreenPos!!.x - captureArea.x, ci.ptScreenPos!!.y - captureArea.y, ci.hCursor!!, 0, 0, 0, null, DI_NORMAL)
		}
		
		val bi = WinGDI.BITMAPINFO(40)
		bi.bmiHeader.biSize = 40
		val ok = GDI32.GetDIBits(blitDC, outputBitmap, 0, captureArea.height, null as ByteArray?, bi, DIB_RGB_COLORS)
		if (ok) {
			val bih = bi.bmiHeader
			bih.biHeight = -Math.abs(bih.biHeight)
			bi.bmiHeader.biCompression = 0
			return bufferedImageFromBitmap(blitDC, outputBitmap, bi)
		} else {
			throw RuntimeException("This should never be null")
		}
	}
	
	override fun resize(x: Int, y: Int, width: Int, height: Int) {
		outputBitmap = GDI32.CreateCompatibleBitmap(windowDC, captureArea.width, captureArea.height)
		GDI32.SelectObject(blitDC, outputBitmap)
	}
	
	override fun destroy() {
		GDI32.DeleteObject(windowDC)
		GDI32.DeleteObject(outputBitmap)
		GDI32.DeleteObject(blitDC)
	}
	
	private fun bufferedImageFromBitmap(blitDC: WinDef.HDC, outputBitmap: WinDef.HBITMAP, bi: WinGDI.BITMAPINFO): BufferedImage {
		val bih = bi.bmiHeader
		val height = Math.abs(bih.biHeight)
		val cm: ColorModel
		val buffer: DataBuffer
		val raster: WritableRaster
		val strideBits = bih.biWidth * bih.biBitCount
		val strideBytesAligned = (strideBits - 1 or 0x1F) + 1 shr 3
		val strideElementsAligned: Int
		when (bih.biBitCount.toInt()) {
			16 -> {
				strideElementsAligned = strideBytesAligned / 2
				cm = DirectColorModel(16, 0x7C00, 0x3E0, 0x1F)
				buffer = DataBufferUShort(strideElementsAligned * height)
				raster = Raster.createPackedRaster(buffer, bih.biWidth, height, strideElementsAligned, cm.masks, null)
			}
			32 -> {
				strideElementsAligned = strideBytesAligned / 4
				cm = DirectColorModel(32, 0xFF0000, 0xFF00, 0xFF)
				buffer = DataBufferInt(strideElementsAligned * height)
				raster = Raster.createPackedRaster(buffer, bih.biWidth, height, strideElementsAligned, cm.masks, null)
			}
			else -> throw IllegalArgumentException("Unsupported bit count: " + bih.biBitCount)
		}
		val ok: Boolean
		when (buffer.dataType) {
			DataBuffer.TYPE_INT -> {
				val pixels = (buffer as DataBufferInt).data
				ok = GDI32.GetDIBits(blitDC, outputBitmap, 0, raster.height, pixels, bi, 0)
			}
			DataBuffer.TYPE_USHORT -> {
				val pixels = (buffer as DataBufferUShort).data
				ok = GDI32.GetDIBits(blitDC, outputBitmap, 0, raster.height, pixels, bi, 0)
			}
			else -> throw AssertionError("Unexpected buffer element type: " + buffer.dataType)
		}
		if (ok) {
			return BufferedImage(cm, raster, false, null)
		} else {
			throw AssertionError("This should never be null!")
		}
	}
	
}