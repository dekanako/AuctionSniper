package io.github.dekanako.domain

import io.github.dekanako.domain.AuctionEventListener.PriceSource.FromSniper
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot.SniperStatus.*

class AuctionSniper(
    itemId: String,
    private val auction: Auction,
) : AuctionEventListener {

    private var sniperListener: SniperListener? = null

    var snapshot: SniperSnapshot = SniperSnapshot.joining(itemId)
        private set(value) {
            field = value
            sniperListener?.sniperStateChanged(field)
        }

    override fun closed() {
        changeSnapshotTo(snapshot.closed())
    }

    override fun currentPrice(price: Int, increment: Int, source: AuctionEventListener.PriceSource) {
        if (source == FromSniper) {
            changeSnapshotTo(snapshot.winning(price))
        } else {
            val bid = price.plus(increment)
            auction.bid(bid)
            changeSnapshotTo(snapshot.bidding(price, bid))
        }
    }
    private fun changeSnapshotTo(newSnapshot: SniperSnapshot) {
        snapshot = newSnapshot
    }

    fun addSniperListener(sniperListener: SniperListener) {
        this.sniperListener = sniperListener
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

        fun isForSameItemAs(sniperSnapshot: SniperSnapshot): Boolean = itemId == sniperSnapshot.itemId

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
