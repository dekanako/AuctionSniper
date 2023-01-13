package io.github.dekanako

import io.github.dekanako.domain.*
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot
import io.github.dekanako.ui.MainWindow
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
        chat.addMessageListener(
            AuctionMessageTranslator(
                connection.user, AuctionSniper(
                    itemId, auction, SniperStateDisplayer()
                )
            )
        )
        auction.join()
    }


    private fun disconnectWhenUIClose(connection: XMPPConnection) {
        ui.addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                connection.disconnect()
            }
        })
    }

    private inner class SniperStateDisplayer : SniperListener {
        override fun sniperStateChanged(sniperSnapshot: SniperSnapshot) {
            SwingUtilities.invokeLater {
                ui.sniperStatusChanged(sniperSnapshot)
            }
        }
    }
}
