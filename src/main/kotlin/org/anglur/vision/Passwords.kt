package org.anglur.vision

import java.util.concurrent.ThreadLocalRandom

fun generatePass() = 1000 + ThreadLocalRandom.current().nextInt(9000)