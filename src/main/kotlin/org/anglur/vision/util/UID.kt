package org.anglur.vision.util

import javafx.beans.property.SimpleStringProperty
import org.anglur.vision.gen.impl.UIDGenerator
import org.anglur.vision.util.extensions.splitEvery

object UID {
	
	private val currentValue by lazy {
		id = generator.generate().splitEvery(3)
		val currentValue = SimpleStringProperty()
		currentValue.set(id)
		
		currentValue
	}
	
	val generator = UIDGenerator()
	
	private lateinit var id: String
	
	fun property() = currentValue
	
	fun create() = id
	
}