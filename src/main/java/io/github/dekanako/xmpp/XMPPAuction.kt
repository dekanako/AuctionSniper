package io.github.dekanako.xmpp

import io.github.dekanako.domain.Auction
import io.github.dekanako.domain.AuctionEventListener

import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException

const val JOIN_COMMAND_FORMAT: String = "SOLVersion: 1.1; Command: JOIN;"
const val BID_FORMAT = "SOLVersion: 1.1; EVENT: BID; PRICE: %s;"
class XMPPAuction(private val connection: XMPPConnection, itemId: String) : Auction {
    private val chat = connection.chatManager.createChat(auctionId(itemId, connection.serviceName), null)
    override fun join() {
        chat.sendMessage(JOIN_COMMAND_FORMAT)
    }
    override fun bid(amount: Int) {
        try {
            chat.sendMessage(String.format(BID_FORMAT, amount))
        } catch (e: XMPPException) {
            e.printStackTrace()
        }
    }

    override fun addAuctionEventListener(auctionSniper: AuctionEventListener) {
        chat.addMessageListener(AuctionMessageTranslator(connection.user, auctionSniper))
    }

}

private fun auctionId(itemId: String, serviceName: String?) = String.format(AUCTION_ID_FORMAT, itemId, serviceName)
