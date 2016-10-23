package org.anglur.vision.guid

import javafx.beans.property.SimpleStringProperty
import org.anglur.vision.guid.generators.impl.PasswordGenerator

object Password {
	
	val property = SimpleStringProperty()
	
	val generator = PasswordGenerator()
	
	init {
		new()
	}
	
	fun get() = property.get()
	
	fun new(): String {
		val password = generator.generate()
		property.set(password)
		return password
	}
	
}