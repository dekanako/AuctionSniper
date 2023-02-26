package io.github.dekanako

import io.github.dekanako.ui.MainWindow

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.SwingUtilities

class Main {
    private lateinit var ui: MainWindow

    private val portfolio = SniperPortfolio()
    init {
        startUserInterface()
    }

    private fun startUserInterface() {
        SwingUtilities.invokeAndWait {
            ui = MainWindow(portfolio)
        }
    }

    fun addUserRequestListenerFor(auctionHouse: AuctionHouse) {
        ui.addUserRequestListener(SniperLauncher(auctionHouse, portfolio))
    }

    fun disconnectWhenUIClose(auctionHouse: AuctionHouse) {
        ui.addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                auctionHouse.disconnect()
            }
        })
    }
}

