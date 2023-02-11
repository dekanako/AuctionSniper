package io.github.dekanako

import io.github.dekanako.xmpp.XMPPAuctionHouse

const val ARG_HOST_NAME = 0
const val ARG_PORT = 1
const val ARG_USER_NAME = 2
const val ARG_PASSWORD = 3
fun main(args: Array<String>) {
    val auctionHouse: AuctionHouse = XMPPAuctionHouse.connect(
        args[ARG_HOST_NAME], args[ARG_PORT], args[ARG_USER_NAME], args[ARG_PASSWORD]
    )

    val main = Main()
    main.disconnectWhenUIClose(auctionHouse)
    main.addUserRequestListenerFor(auctionHouse)
}