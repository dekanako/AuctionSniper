package io.github.dekanako.domain

interface Auction {
    fun bid(amount: Int)
    fun join()
}
