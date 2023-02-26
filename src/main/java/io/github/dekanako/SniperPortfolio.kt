package io.github.dekanako

import io.github.dekanako.domain.AuctionSniper
import io.github.dekanako.ui.PortfolioListener

class SniperPortfolio : SniperCollector {

    private val notToBeGCD: MutableList<AuctionSniper> = mutableListOf()

    private var listener: PortfolioListener? = null

    override fun addSniper(sniper: AuctionSniper) {
        listener?.sniperAdded(sniper)
        notToBeGCD.add(sniper)
    }

    fun addPortfolioListener(listener: PortfolioListener) {
        this.listener = listener
    }

}