package org.anglur.vision.view

import javafx.scene.Scene
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
			scene.stylesheets.addAll(FX.stylesheets)
			show()
		}
	}

}