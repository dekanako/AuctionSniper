package io.github.dekanako.domain

interface AuctionEventListener {
    enum class PriceSource {
        FromSniper, FromOtherBidder
    }

    fun closed()
    fun currentPrice(price: Int, increment: Int, source: PriceSource)
}
