package e2e

import io.github.dekanako.domain.AuctionSniper.SniperSnapshot.SniperStatus.*
import io.github.dekanako.main
import io.github.dekanako.ui.APPLICATION_TITLE
import io.github.dekanako.ui.MAIN_WINDOW_NAME
import io.github.dekanako.ui.SniperTableModel.Companion.textFor
import kotlin.concurrent.thread

class ApplicationRunner {
    private val driver = AuctionSniperDriver(1000)
    private lateinit var itemId: String
    fun startBiddingIn(auctionServer: FakeAuctionServer) {
        thread(isDaemon = true, name = "Test Application") {
            main(arrayOf(HOST_NAME, "5224", "sniper", "sniper", auctionServer.itemID))
        }
        itemId = auctionServer.itemID
        driver.hasTitle(APPLICATION_TITLE)
        driver.hasColumnTitles()
        driver.showsSniperStatus("",0,0, textFor(JOINING))
    }

    fun showsSniperHasLostAuction(lastPrice: Int, bid: Int) {
        driver.showsSniperStatus(itemId, lastPrice, bid, textFor(LOST))
    }

    fun hasShownSniperIsBidding(lastPrice: Int, lastBid: Int) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid, textFor(BIDDING))
    }

    fun hasShownSniperIsWinning(winningBid: Int) {
        driver.showsSniperStatus(itemId, winningBid, winningBid, textFor(WINNING))
    }

    fun showsSniperHasWonAuction(lastPrice: Int) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, textFor(WON))
    }

    fun stop() {
        driver.dispose()
    }

    companion object {
        const val SNIPER_XMPP_ID: String = "sniper@localhost/Auction"
    }

}
