package io.github.dekanako

import io.github.dekanako.domain.AuctionSniper
import io.github.dekanako.ui.UserRequestListener

class SniperLauncher(
    private val auctionHouse: AuctionHouse,
    private val collector: SniperCollector,
) : UserRequestListener {

    override fun joinAuction(itemID: String) {
        val auction = auctionHouse.auctionFor(itemID)
        val sniper = AuctionSniper(itemID, auction)
        auction.addAuctionEventListener(sniper)
        collector.addSniper(sniper)
        auction.join()
    }

}
