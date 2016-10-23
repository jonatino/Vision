package org.anglur.vision.guid.generators.impl

import org.anglur.vision.guid.generators.Generator
import java.util.concurrent.ThreadLocalRandom.current as tlr

class PasswordGenerator : Generator() {
	
	override fun generate() = String.format("%04d", tlr().nextInt(10000))
	
}