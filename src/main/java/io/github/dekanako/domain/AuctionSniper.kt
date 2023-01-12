package io.github.dekanako.domain

import io.github.dekanako.domain.AuctionEventListener.PriceSource.FromSniper

class AuctionSniper(private val auction: Auction, private val sniperListener: SniperListener) : AuctionEventListener {
    private var isWinning: Boolean = false
    override fun closed() {
        if (isWinning) {
            sniperListener.sniperWon()
        } else
            sniperListener.sniperLost()
    }


    override fun currentPrice(price: Int, increment: Int, source: AuctionEventListener.PriceSource) {
        isWinning = source == FromSniper
        if (isWinning)
            sniperListener.sniperWinning()
        else {
            auction.bid(price.plus(increment))
            sniperListener.sniperBidding()
        }
    }
}
