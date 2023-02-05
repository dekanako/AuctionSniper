package io.github.dekanako.xmpp

import io.github.dekanako.domain.Auction
import io.github.dekanako.domain.AuctionSniper

import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException

val AUCTION_RESOURCE = "Auction"
val ITEM_ID_AS_LOGIN = "%s"
val AUCTION_ID_FORMAT = "$ITEM_ID_AS_LOGIN@%s/$AUCTION_RESOURCE"
class XMPPAuction(private val connection: XMPPConnection, itemID: String) : Auction {


    private val chat = connection.chatManager.createChat(auctionId(itemID, connection.serviceName), null)

    override fun bid(amount: Int) {
        try {
            chat.sendMessage(String.format(BID_FORMAT, amount))
        } catch (e: XMPPException) {
            e.printStackTrace()
        }
    }

    override fun join() {
        chat.sendMessage(JOIN_COMMAND_FORMAT)
    }

    override fun addAuctionEventListener(auctionSniper: AuctionSniper) {
        chat.addMessageListener(AuctionMessageTranslator(connection.user, auctionSniper))
    }
    private fun auctionId(itemId: String, serviceName: String?) = String.format(AUCTION_ID_FORMAT, itemId, serviceName)

}