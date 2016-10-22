package org.anglur.vision.gen.impl

import org.anglur.vision.gen.Generator
import java.util.concurrent.ThreadLocalRandom.current as tlr

class PasswordGenerator : Generator() {
	
	override fun generate() = String.format("%04d", tlr().nextInt(10000))
	
}