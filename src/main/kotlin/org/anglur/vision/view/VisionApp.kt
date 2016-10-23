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

package org.anglur.vision.view

import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.App
import tornadofx.FX
import tornadofx.find

class VisionApp : App() {
	
	override val primaryView = VisionGUI::class

	override fun start(stage: Stage) {
		FX.primaryStage = stage
		FX.application = this

		val view = find(primaryView)
		stage.apply {
			scene = Scene(view.root)
			icons.add(Image(VisionGUI::class.java.getResource("../../../../icon.png").toExternalForm()))
			scene.stylesheets.addAll(FX.stylesheets)
			show()
		}
	}

}