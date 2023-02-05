package io.github.dekanako

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.SwingUtilities

import org.jivesoftware.smack.XMPPConnection

import io.github.dekanako.ui.MainWindow
import io.github.dekanako.ui.SniperTableModel
import io.github.dekanako.ui.UserRequestListener

import io.github.dekanako.xmpp.XMPPAuction

import io.github.dekanako.domain.*
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot

class Main(private val connection: XMPPConnection) : UserRequestListener {
    private lateinit var ui: MainWindow
    private val snipers = SniperTableModel()

    @Suppress("Not to be GCD")
    private var notToBeGCD: MutableList<Auction> = mutableListOf()

    init {
        startUserInterface()
    }

    private fun startUserInterface() {
        SwingUtilities.invokeAndWait {
            ui = MainWindow(snipers, this)
        }
    }

    override fun joinAuction(itemID: String) {
        disconnectWhenUIClose(connection)

        snipers.addSniper(SniperSnapshot.joining(itemID))

        val auction = XMPPAuction(connection, itemID)

        notToBeGCD.add(auction)

        auction.addAuctionEventListener(AuctionSniper(itemID, auction, SwingThreadSniperListener()))
        auction.join()
    }

    private fun disconnectWhenUIClose(connection: XMPPConnection) {
        ui.addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                connection.disconnect()
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
