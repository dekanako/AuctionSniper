package io.github.dekanako.domain

interface SniperListener {
    fun sniperStateChanged(sniperSnapshot: AuctionSniper.SniperSnapshot)
}
