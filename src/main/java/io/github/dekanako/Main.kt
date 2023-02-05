package io.github.dekanako

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.SwingUtilities

import io.github.dekanako.ui.MainWindow
import io.github.dekanako.ui.SniperTableModel
import io.github.dekanako.ui.UserRequestListener

import io.github.dekanako.domain.*
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot

class Main  {
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

    fun disconnectWhenUIClose(auctionHouse: AuctionHouse) {
        ui.addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                auctionHouse.disconnect()
            }
        })
    }

    fun addUserRequestListenerFor(auctionHouse: AuctionHouse) {
        ui.addUserRequestListener(object : UserRequestListener {
            override fun joinAuction(itemID: String) {
                snipers.addSniper(SniperSnapshot.joining(itemID))

                val auction = auctionHouse.auctionFor(itemID)
                notToBeGCD.add(auction)
                auction.addAuctionEventListener(AuctionSniper(itemID, auction, SwingThreadSniperListener()))
                auction.join()
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
