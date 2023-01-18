package e2e

import io.github.dekanako.domain.AuctionSniper.SniperSnapshot.SniperStatus.*
import io.github.dekanako.main
import io.github.dekanako.ui.APPLICATION_TITLE
import io.github.dekanako.ui.SniperTableModel.Companion.textFor
import kotlin.concurrent.thread

class ApplicationRunner {
    private val driver = AuctionSniperDriver(1000)
    fun startBiddingIn(vararg auctions: FakeAuctionServer) {
        thread(isDaemon = true, name = "Test Application") {
            main(args = arguments(auctions))
        }
        assertBasicWindow(auctions)
    }

    private fun assertBasicWindow(auctions: Array<out FakeAuctionServer>) {
        driver.hasTitle(APPLICATION_TITLE)
        driver.hasColumnTitles()
        driver.showsSniperJoiningAuctions(auctions)
    }

    private fun arguments(auctions: Array<out FakeAuctionServer>): Array<String> {
        val args = mutableListOf<String>()
        args.add(HOST_NAME)
        args.add("5224")
        args.add("sniper")
        args.add("sniper")
        auctions.forEach {
            args.add(it.itemID)
        }
        return args.toTypedArray()
    }

    fun showsSniperHasLostAuction(auction: FakeAuctionServer, lastPrice: Int, bid: Int) {
        driver.showsSniperStatus(auction.itemID, lastPrice, bid, textFor(LOST))
    }

    fun hasShownSniperIsBidding(auction: FakeAuctionServer, lastPrice: Int, lastBid: Int) {

        driver.showsSniperStatus(auction.itemID, lastPrice, lastBid, textFor(BIDDING))
    }

    fun hasShownSniperIsWinning(auction: FakeAuctionServer, winningBid: Int) {
        driver.showsSniperStatus(auction.itemID, winningBid, winningBid, textFor(WINNING))
    }

    fun showsSniperHasWonAuction(auction: FakeAuctionServer, lastPrice: Int) {
        driver.showsSniperStatus(auction.itemID, lastPrice, lastPrice, textFor(WON))
    }

    fun stop() {
        driver.dispose()
    }

    companion object {
        const val SNIPER_XMPP_ID: String = "sniper@localhost/Auction"
    }

}
