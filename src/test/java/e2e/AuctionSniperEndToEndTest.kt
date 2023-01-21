package e2e

import e2e.ApplicationRunner.Companion.SNIPER_XMPP_ID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class AuctionSniperEndToEndTest {

    private val auction = NewAuctionServer("dish")
    private val auction2 = NewAuctionServer("tv")
    private val application = ApplicationRunner()

    @Test
    fun sniperJoinsAuctionTillAuctionIsClosed() {
        auction.startSellingItem()

        application.startBiddingIn(auction)

        auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

        auction.announceClosed()

        application.showsSniperHasLostAuction(auction, 0, 0)
    }

    @Test
    fun sniperBidsInAnAuctionAndLose() {
        auction.startSellingItem()

        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

        auction.reportPrice(10, 10, "other bidder")

        application.hasShownSniperIsBidding(auction,10,20)
        auction.hasReceivedBid(20, SNIPER_XMPP_ID)

        auction.announceClosed()

        application.showsSniperHasLostAuction(auction, 10, 20)
    }

    @Test
    fun sniperWinsAnAuctionByBiddingHigher() {
        auction.startSellingItem()

        application.startBiddingIn(auction)
        auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

        auction.reportPrice(1000, 98, "Other Bidder")
        application.hasShownSniperIsBidding(auction, 1000, 1098)
        auction.hasReceivedBid(1098, SNIPER_XMPP_ID)


        auction.reportPrice(1098, 97, SNIPER_XMPP_ID)
        application.hasShownSniperIsWinning(auction, 1098)

        auction.announceClosed()

        application.showsSniperHasWonAuction(auction, 1098)
    }

    @Test
    fun sniperBidsForMultipleItem() {
        auction.startSellingItem()
        auction2.startSellingItem()

        application.startBiddingIn(auction, auction2)

        auction.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)
        auction2.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)

        auction.reportPrice(1000, 98, "Other Bidder")
        auction.hasReceivedBid(1098, SNIPER_XMPP_ID)

        auction2.reportPrice(500, 21, "Other Bidder")
        auction2.hasReceivedBid(521, SNIPER_XMPP_ID)

        application.hasShownSniperIsBidding(auction,1000, 1098)
        application.hasShownSniperIsBidding(auction2,500, 521)

        auction.reportPrice(1098, 97, SNIPER_XMPP_ID)
        application.hasShownSniperIsWinning(auction, 1098)

        auction2.reportPrice(521, 22, SNIPER_XMPP_ID)
        application.hasShownSniperIsWinning(auction2, 521)


        auction.announceClosed()
        application.showsSniperHasWonAuction(auction, 1098)

        auction2.announceClosed()
        application.showsSniperHasWonAuction(auction2, 521)
    }

    @AfterEach
    fun stop() {
        auction.stop()
        application.stop()
    }
}