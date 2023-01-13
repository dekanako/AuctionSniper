package e2e

import e2e.ApplicationRunner.Companion.SNIPER_XMPP_ID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class AuctionSniperEndToEndTest {

    private val auction = FakeAuctionServer("item-54321")
    private val application = ApplicationRunner()

    @Test
    fun sniperJoinsAuctionTillAuctionIsClosed() {
        auction.startSellingItem()

        application.startBiddingIn(auction)

        auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

        auction.announceClosed()

        application.showsSniperHasLostAuction(0, 0)
    }

    @Test
    fun sniperBidsInAnAuctionAndLose() {
        auction.startSellingItem()

        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

        auction.reportPrice(10, 10, "other bidder")

        application.hasShownSniperIsBidding(10,20)
        auction.hasReceivedBid(20, SNIPER_XMPP_ID)

        auction.announceClosed()

        application.showsSniperHasLostAuction(10, 20)
    }

    @Test
    fun sniperWinsAnAuctionByBiddingHigher() {
        auction.startSellingItem()

        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

        auction.reportPrice(1000, 98, "Other Bidder")
        application.hasShownSniperIsBidding(1000, 1098)
        auction.hasReceivedBid(1098, SNIPER_XMPP_ID)


        auction.reportPrice(1098, 97, SNIPER_XMPP_ID)
        application.hasShownSniperIsWinning(1098)

        auction.announceClosed()

        application.showsSniperHasWonAuction(1098)
    }

    @AfterEach
    fun stop() {
        auction.stop()
        application.stop()
    }
}