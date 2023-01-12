package io.github.dekanako

import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.XMPPConnection

const val ARG_HOST_NAME = 0
const val ARG_PORT = 1
val ARG_USER_NAME = 2
val ARG_PASSWORD = 3
val ARG_ITEM_ID = 4

val AUCTION_RESOURCE = "Auction"
val ITEM_ID_AS_LOGIN = "auction-%s"
val AUCTION_ID_FORMAT = "$ITEM_ID_AS_LOGIN@%s/$AUCTION_RESOURCE"
object MainRun {

    const val JOIN_COMMAND_FORMAT: String = ""
    const val BID_FORMAT = "SOLVersion: 1.1; EVENT: BID; PRICE: %s;"
}
fun main(args: Array<String>) {
    val main = Main()
    main.joinAuction(
        connection(args[ARG_HOST_NAME], args[ARG_PORT], args[ARG_USER_NAME], args[ARG_PASSWORD]),
        args[ARG_ITEM_ID]
    )
}

private fun connection(hostname: String, port: String, userName: String, password: String): XMPPConnection {
    return XMPPConnection(ConnectionConfiguration(hostname, port.toInt())).apply {
        connect()
        login(userName, password, AUCTION_RESOURCE)
    }
}

fun auctionId(itemId: String, serviceName: String?) = String.format(AUCTION_ID_FORMAT, itemId, serviceName)
