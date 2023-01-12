package io.github.dekanako.domain

interface SniperListener {
    fun sniperLost()
    fun sniperBidding()
    fun sniperWinning()
    fun sniperWon()
}
