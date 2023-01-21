package e2e

import io.github.dekanako.domain.AuctionSniper.SniperSnapshot.SniperStatus.*
import io.github.dekanako.main
import io.github.dekanako.ui.APPLICATION_TITLE
import io.github.dekanako.ui.SniperTableModel.Companion.textFor
import kotlin.concurrent.thread

class ApplicationRunner {
    private val driver = AuctionSniperDriver(1000)
    fun startBiddingIn(vararg auctions: ItemID) {
        startSniper()
        auctions.forEach { itemId ->
            driver.startBiddingInFor(itemId.itemID())
            driver.showsSniperStatus(itemId.itemID(), 0, 0, textFor(JOINING))
        }
        driver.hasTitle(APPLICATION_TITLE)
        driver.hasColumnTitles()
    }

    private fun startSniper() {
        thread(isDaemon = true, name = "Test Application") {
            main(args = arguments())
        }
    }



    private fun arguments(): Array<String> {
        val args = mutableListOf<String>()
        args.add(HOST_NAME)
        args.add("5224")
        args.add("sniper")
        args.add("sniper")
        return args.toTypedArray()
    }

    fun showsSniperHasLostAuction(auction: ItemID, lastPrice: Int, bid: Int) {
        driver.showsSniperStatus(auction.itemID(), lastPrice, bid, textFor(LOST))
    }

    fun hasShownSniperIsBidding(auction: ItemID, lastPrice: Int, lastBid: Int) {

        driver.showsSniperStatus(auction.itemID(), lastPrice, lastBid, textFor(BIDDING))
    }

    fun hasShownSniperIsWinning(auction: ItemID, winningBid: Int) {
        driver.showsSniperStatus(auction.itemID(), winningBid, winningBid, textFor(WINNING))
    }

    fun showsSniperHasWonAuction(auction: ItemID, lastPrice: Int) {
        driver.showsSniperStatus(auction.itemID(), lastPrice, lastPrice, textFor(WON))
    }

    fun stop() {
        driver.dispose()
    }

    companion object {
        const val SNIPER_XMPP_ID: String = "sniper@localhost/Auction"
    }

}
