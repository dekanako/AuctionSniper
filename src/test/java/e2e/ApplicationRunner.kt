package e2e

import io.github.dekanako.Main
import io.github.dekanako.main
import kotlin.concurrent.thread

class ApplicationRunner {
    private val driver = AuctionSniperDriver(1000)

    fun startBiddingIn(auctionServer: FakeAuctionServer) {
        thread(isDaemon = true, name = "Test Application") {
            main(arrayOf(HOST_NAME, "5224", "sniper", "sniper", auctionServer.itemID))
        }
        driver.showsSniperStatus(Main.STATUS_JOINING)
    }

    fun showsSniperHasLostAuction() {
        driver.showsSniperStatus(Main.STATUS_LOST)
    }

    fun hasShownSniperIsBidding() {
        driver.showsSniperStatus(Main.STATUS_BIDDING)
    }

    fun stop() {
        driver.dispose()
    }

    fun hasShownSniperIsWinning() {
        driver.showsSniperStatus(Main.STATUS_WINNING)
    }

    fun showsSniperHasWonAuction() {
        driver.showsSniperStatus(Main.STATUS_WON)
    }

    companion object {
        const val SNIPER_XMPP_ID: String = "sniper@localhost/Auction"
    }

}
