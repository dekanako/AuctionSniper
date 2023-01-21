package io.github.dekanako

import io.github.dekanako.domain.*
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot
import io.github.dekanako.ui.MainWindow
import io.github.dekanako.infraRemote.AuctionMessageTranslator
import io.github.dekanako.infraRemote.XMPPAuction
import io.github.dekanako.ui.UserRequestListener
import io.github.dekanako.ui.SniperTableModel
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.XMPPConnection
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.SwingUtilities

class Main(private val connection: XMPPConnection) : UserRequestListener {
    private lateinit var ui: MainWindow
    private val snipers = SniperTableModel()

    @Suppress("Not to be GCD")
    private var notToBeGCD: MutableList<Chat> = mutableListOf()

    init {
        startUserInterface()
    }

    private fun startUserInterface() {
        SwingUtilities.invokeAndWait {
            ui = MainWindow(snipers, this)
        }
    }

    override fun joinAuction(itemID: String) {
        joinAuction(connection, itemID)
    }

    private fun joinAuction(connection: XMPPConnection, itemId: String) {
        disconnectWhenUIClose(connection)
        snipers.addSniper(SniperSnapshot.joining(itemId))
        val chat = connection.chatManager.createChat(auctionId(itemId, connection.serviceName), null)

        notToBeGCD.add(chat)

        val auction = XMPPAuction(chat)
        chat.addMessageListener(
            AuctionMessageTranslator(connection.user, AuctionSniper(itemId, auction, SwingThreadSniperListener()))
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

    private inner class SwingThreadSniperListener : SniperListener {
        override fun sniperStateChanged(sniperSnapshot: SniperSnapshot) {
            SwingUtilities.invokeLater {
                snipers.sniperStatusChanged(sniperSnapshot)
            }
        }
    }


}
