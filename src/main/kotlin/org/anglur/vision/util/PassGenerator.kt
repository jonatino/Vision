package org.anglur.vision.util

import java.util.concurrent.ThreadLocalRandom.current as tlr

object PassGenerator {

	fun generatePass() = String.format("%04d", tlr().nextInt(10000))

}