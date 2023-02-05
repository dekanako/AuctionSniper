package io.github.dekanako.xmpp

import io.github.dekanako.MainRun
import io.github.dekanako.auctionId

import io.github.dekanako.domain.Auction
import io.github.dekanako.domain.AuctionSniper

import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException

class XMPPAuction(private val connection: XMPPConnection, itemID: String) : Auction {

    private val chat = connection.chatManager.createChat(auctionId(itemID, connection.serviceName), null)

    override fun bid(amount: Int) {
        try {
            chat.sendMessage(String.format(MainRun.BID_FORMAT, amount))
        } catch (e: XMPPException) {
            e.printStackTrace()
        }
    }

    override fun join() {
        chat.sendMessage(MainRun.JOIN_COMMAND_FORMAT)
    }

    override fun addAuctionEventListener(auctionSniper: AuctionSniper) {
        chat.addMessageListener(AuctionMessageTranslator(connection.user, auctionSniper))
    }
}