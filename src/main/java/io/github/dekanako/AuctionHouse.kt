package io.github.dekanako

import io.github.dekanako.domain.Auction

interface AuctionHouse {
    fun auctionFor(itemId: String): Auction
    fun disconnect()
}
