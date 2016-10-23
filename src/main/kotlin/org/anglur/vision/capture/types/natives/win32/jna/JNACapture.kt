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
	
	private val windowDC = User32.GetDC(User32.GetDesktopWindow())
	private val blitDC = GDI32.CreateCompatibleDC(windowDC)
	private val outputBitmap by lazy {
		val bitmap = GDI32.CreateCompatibleBitmap(windowDC, width, height)
		GDI32.SelectObject(blitDC, bitmap)
		
		bitmap
	}
	
	override fun capture(): BufferedImage {
		GDI32.BitBlt(blitDC, 0, 0, width, height, windowDC, x, y, GDI32.SRCCOPY)
		
		if (showCursor) {
			val ci = CURSORINFO()
			ci.cbSize = ci.size()
			if (User32.GetCursorInfo(ci))
				User32.DrawIconEx(blitDC, ci.ptScreenPos!!.x - x, ci.ptScreenPos!!.y - y, ci.hCursor!!, 0, 0, 0, null, DI_NORMAL)
		}
		
		val bi = WinGDI.BITMAPINFO(40)
		bi.bmiHeader.biSize = 40
		val ok = GDI32.GetDIBits(blitDC, outputBitmap, 0, height, null as ByteArray?, bi, DIB_RGB_COLORS)
		if (ok) {
			val bih = bi.bmiHeader
			bih.biHeight = -Math.abs(bih.biHeight)
			bi.bmiHeader.biCompression = 0
			return bufferedImageFromBitmap(blitDC, outputBitmap, bi)
		} else {
			throw RuntimeException("This should never be null rofl")
		}
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