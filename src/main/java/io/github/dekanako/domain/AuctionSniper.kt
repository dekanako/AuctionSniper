package io.github.dekanako.domain

import io.github.dekanako.domain.AuctionEventListener.PriceSource.FromSniper
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot.SniperStatus.*

class AuctionSniper(
    private val itemId: String,
    private val auction: Auction,
    private val sniperListener: SniperListener,
) : AuctionEventListener {

    private var sniperSnapshot: SniperSnapshot = SniperSnapshot.joining(itemId)
        set(value) {
            field = value
            sniperListener.sniperStateChanged(field)
        }
    override fun closed() {
        changeSnapshotTo(sniperSnapshot.closed())
    }

    override fun currentPrice(price: Int, increment: Int, source: AuctionEventListener.PriceSource) {
        if (source == FromSniper) {
            changeSnapshotTo(sniperSnapshot.winning(price))
        } else {
            val bid = price.plus(increment)
            auction.bid(bid)
            changeSnapshotTo(sniperSnapshot.bidding(price, bid))
        }
    }
    private fun changeSnapshotTo(newSnapshot: SniperSnapshot) {
        sniperSnapshot = newSnapshot
    }

    data class SniperSnapshot(val itemId: String, val price: Int, val bid: Int, val state: SniperStatus) {
        fun closed(): SniperSnapshot {
            val newStatus = if(state == WINNING) WON else LOST
            return SniperSnapshot(itemId, price, bid, newStatus)
        }

        fun winning(price: Int): SniperSnapshot = SniperSnapshot(itemId, price, price, WINNING)

        fun bidding(price: Int, bid: Int): SniperSnapshot {
            return SniperSnapshot(itemId, price, bid, BIDDING)
        }

        enum class SniperStatus {
            JOINING,
            BIDDING,
            WINNING,
            LOST,
            WON
        }
        companion object {
            fun joining(itemId: String) = SniperSnapshot(itemId, 0, 0, JOINING)
        }
    }

}
