package io.github.dekanako

import io.github.dekanako.domain.AuctionSniper

interface SniperCollector {
    fun addSniper(sniper: AuctionSniper)
}
