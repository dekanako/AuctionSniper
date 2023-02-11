package io.github.dekanako

import io.github.dekanako.domain.*
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot

import io.github.dekanako.ui.MainWindow
import io.github.dekanako.ui.SniperTableModel

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.SwingUtilities

class Main {
    private lateinit var ui: MainWindow
    private val snipers = SniperTableModel()

    @Suppress("Not to be GCD")
    private var notToBeGCD: MutableList<Auction> = mutableListOf()

    init {
        startUserInterface()
    }

    private fun startUserInterface() {
        SwingUtilities.invokeAndWait {
            ui = MainWindow(snipers)
        }
    }

    fun addUserRequestListenerFor(auctionHouse: AuctionHouse) {
        ui.addUserRequestListener { itemID ->
            snipers.addSniper(SniperSnapshot.joining(itemID))
            val auction = auctionHouse.auctionFor(itemID)
            notToBeGCD.add(auction)
            auction.addAuctionEventListener(
                AuctionSniper(itemID, auction, SwingThreadSniperListener())
            )
            auction.join()
        }
    }

    fun disconnectWhenUIClose(auctionHouse: AuctionHouse) {
        ui.addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                auctionHouse.disconnect()
            }
        })
    }

    private inner class SwingThreadSniperListener : SniperListener {
        override fun sniperStateChanged(sniperSnapshot: SniperSnapshot) {
            SwingUtilities.invokeLater {
                snipers.sniperStatusChanged(sniperSnapshot)
            }
        }
    }
}