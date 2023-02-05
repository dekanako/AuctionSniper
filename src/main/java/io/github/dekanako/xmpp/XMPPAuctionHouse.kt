package io.github.dekanako.xmpp

import io.github.dekanako.AuctionHouse
import io.github.dekanako.domain.Auction
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.XMPPConnection

const val JOIN_COMMAND_FORMAT: String = "SOLVersion: 1.1; Command: JOIN;"
const val BID_FORMAT = "SOLVersion: 1.1; EVENT: BID; PRICE: %s;"
class XMPPAuctionHouse(private val connection: XMPPConnection) : AuctionHouse {

    override fun auctionFor(itemId: String): Auction {
        return XMPPAuction(connection, (itemId))
    }

    override fun disconnect() {
        connection.disconnect()
    }

    companion object {
        fun connect(s: String, s1: String, s2: String, s3: String): AuctionHouse {
            return XMPPAuctionHouse(connection(s, s1, s2, s3))
        }

        private fun connection(hostname: String, port: String, userName: String, password: String): XMPPConnection {
            val connectionConfiguration = ConnectionConfiguration(hostname, port.toInt())
            return XMPPConnection(connectionConfiguration).apply {
                connect()
                login(userName, password, AUCTION_RESOURCE)
            }
        }
    }

}