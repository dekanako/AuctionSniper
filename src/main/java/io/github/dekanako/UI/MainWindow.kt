package io.github.dekanako.UI

import io.github.dekanako.Main
import java.awt.Color.BLACK
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.border.LineBorder

const val MAIN_WINDOW_NAME: String = "main window"
const val SNIPER_STATUS_NAME: String = "sniper state"
class MainWindow: JFrame("Auction Sniper") {
    private val sniperStatus = createLabel(Main.STATUS_JOINING)

    init {
        name = MAIN_WINDOW_NAME
        add(sniperStatus)
        pack()
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
    }

    private fun createLabel(initialText: String): JLabel {
        return JLabel(initialText).apply {
            name = SNIPER_STATUS_NAME
            border = LineBorder(BLACK)
        }
    }

    fun showStatus(statusLost: String) {
        sniperStatus.text = statusLost
    }
}