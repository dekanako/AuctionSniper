package io.github.dekanako

import io.github.dekanako.ui.MainWindow
import io.github.dekanako.ui.SniperTableModel

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.SwingUtilities

class Main {
    private lateinit var ui: MainWindow

    private val snipers = SniperTableModel()
    init {
        startUserInterface()
    }

    private fun startUserInterface() {
        SwingUtilities.invokeAndWait {
            ui = MainWindow(snipers)
        }
    }

    fun addUserRequestListenerFor(auctionHouse: AuctionHouse) {
        ui.addUserRequestListener(SniperLauncher(auctionHouse, snipers))
    }

    fun disconnectWhenUIClose(auctionHouse: AuctionHouse) {
        ui.addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                auctionHouse.disconnect()
            }
        })
    }
}

