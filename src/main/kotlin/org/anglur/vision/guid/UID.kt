package org.anglur.vision.guid

import javafx.beans.property.SimpleStringProperty
import org.anglur.vision.guid.generators.impl.UIDGenerator
import org.anglur.vision.util.extensions.splitEvery

object UID {
	
	val generator = UIDGenerator()
	
	val property by lazy {
		SimpleStringProperty(generator.generate().splitEvery(3))
	}
	
	fun create() = property.get()
	
	
}