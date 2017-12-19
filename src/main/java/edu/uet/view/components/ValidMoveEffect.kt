package edu.uet.view.components

import javafx.scene.effect.BlurType
import javafx.scene.effect.InnerShadow
import javafx.scene.paint.Color

class ValidMoveEffect : InnerShadow() {
    init {
        blurType = BlurType.ONE_PASS_BOX
        choke = 10.0
        width = 30.0
        height = 30.0
        color = Color.valueOf("GREEN")
    }
}