package io.github.dekanako.xmpp

import io.github.dekanako.AuctionHouse
import io.github.dekanako.domain.Auction
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.XMPPConnection

const val AUCTION_RESOURCE = "Auction"
const val ITEM_ID_AS_LOGIN = "%s"
const val AUCTION_ID_FORMAT = "$ITEM_ID_AS_LOGIN@%s/$AUCTION_RESOURCE"

class XMPPAuctionHouse(private val connection: XMPPConnection) : AuctionHouse {
    override fun auctionFor(itemId: String): Auction = XMPPAuction(connection, itemId)

    override fun disconnect() {
        connection.disconnect()
    }

    companion object {
        fun connect(s: String, s1: String, s2: String, s3: String): AuctionHouse {
            val connection = connection(s, s1, s2, s3)

            return XMPPAuctionHouse(connection)
        }
    }

}

fun connection(hostname: String, port: String, userName: String, password: String): XMPPConnection {
    val connectionConfiguration = ConnectionConfiguration(hostname, port.toInt())
    return XMPPConnection(connectionConfiguration).apply {
        connect()
        login(userName, password, AUCTION_RESOURCE)
    }
}