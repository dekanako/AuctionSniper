package io.github.dekanako

import io.github.dekanako.UI.MainWindow
import io.github.dekanako.domain.Auction
import io.github.dekanako.domain.AuctionSniper
import io.github.dekanako.domain.SniperListener
import io.github.dekanako.infraRemote.AuctionMessageTranslator
import io.github.dekanako.infraRemote.XMPPAuction
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.XMPPConnection
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.SwingUtilities

class Main {
    private lateinit var ui: MainWindow

    @Suppress("Not to be GCD")
    private lateinit var chat: Chat

    init {
        startUserInterface()
    }

    private fun startUserInterface() {
        SwingUtilities.invokeAndWait {
            ui = MainWindow()
        }
    }

    fun joinAuction(connection: XMPPConnection, itemId: String) {
        disconnectWhenUIClose(connection)

        chat = connection.chatManager.createChat(auctionId(itemId, connection.serviceName),null)

        val auction: Auction = XMPPAuction(chat)
        chat.addMessageListener(AuctionMessageTranslator(connection.user, AuctionSniper(auction, SniperStateDisplayer())))
        auction.join()
    }


    private fun disconnectWhenUIClose(connection: XMPPConnection) {
        ui.addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                connection.disconnect()
            }
        })
    }

    companion object {
        const val STATUS_BIDDING = "BIDDING"
        const val STATUS_JOINING = "JOIN"
        const val STATUS_LOST = "LOST"
        const val STATUS_WINNING = "WINNING"
        const val STATUS_WON = "WON"
    }

    private inner class SniperStateDisplayer : SniperListener {
        override fun sniperBidding() {
            showStatus(STATUS_BIDDING)
        }

        override fun sniperWinning() {
            showStatus(STATUS_WINNING)
        }

        override fun sniperWon() {
            showStatus(STATUS_WON)
        }

        override fun sniperLost() {
            showStatus(STATUS_LOST)
        }

        private fun showStatus(status: String) {
            SwingUtilities.invokeLater {
                ui.showStatus(status)
            }
        }

    }
}
