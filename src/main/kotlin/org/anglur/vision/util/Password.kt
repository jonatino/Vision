package org.anglur.vision.util

import javafx.beans.property.SimpleStringProperty
import org.anglur.vision.gen.impl.PasswordGenerator

object Password {
	
	private val currentPassword = SimpleStringProperty()
	
	val generator = PasswordGenerator()
	
	init {
		new()
	}
	
	fun property() = currentPassword
	
	fun get() = currentPassword.get()
	
	fun new(): String {
		val password = generator.generate()
		currentPassword.set(password)
		return password
	}
	
}