package unit

import io.github.dekanako.domain.*
import io.github.dekanako.domain.AuctionEventListener.PriceSource
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot.SniperStatus.*
import io.mockk.MockKVerificationScope
import io.mockk.Ordering.SEQUENCE
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

const val ITEM_ID = "item-323"

class AuctionSniperTest {
    private val auction = mockk<Auction>(relaxed = true)
    private val sniperListenerSpy = mockk<SniperListener>(relaxed = true)

    private val sniper = AuctionSniper(ITEM_ID, auction, sniperListenerSpy)

    @Test
    fun reportLostWhenAuctionClosesImmediately() {
        sniper.closed()

        calledExactly {
            sniperListenerSpy.sniperStateChanged(SniperSnapshot(ITEM_ID, 0, 0, LOST))
        }
    }

    @Test
    fun bidWhenPriceReceivedFromOtherBidder() {
        sniper.currentPrice(10, 10, PriceSource.FromOtherBidder)

        calledExactly {
            auction.bid(20)
            sniperListenerSpy.sniperStateChanged(SniperSnapshot(ITEM_ID,10,20, BIDDING))
        }
    }

    @Test
    fun reportLostIfAuctionClosesWhenBidding() {
        sniper.currentPrice(10, 10, PriceSource.FromOtherBidder)
        sniper.closed()
        calledExactly {
            sniperListenerSpy.sniperStateChanged(SniperSnapshot(ITEM_ID, 10, 20, BIDDING))
            sniperListenerSpy.sniperStateChanged(SniperSnapshot(ITEM_ID, 10, 20, LOST))
        }
    }

    @Test
    fun reportWinIfAuctionClosesWhenWinning() {
        sniper.currentPrice(10, 10, PriceSource.FromOtherBidder)
        sniper.currentPrice(20, 10, PriceSource.FromSniper)

        sniper.closed()

        calledExactly {
            sniperListenerSpy.sniperStateChanged(SniperSnapshot(ITEM_ID, 10, 20, BIDDING))
            sniperListenerSpy.sniperStateChanged(SniperSnapshot(ITEM_ID, 20, 20, WINNING))
            sniperListenerSpy.sniperStateChanged(SniperSnapshot(ITEM_ID, 20, 20, WON))
        }
    }

    @Test
    fun reportWinningWhenCurrentPriceComesFromSniper() {
        sniper.currentPrice(123, 12, PriceSource.FromOtherBidder)
        sniper.currentPrice(135, 45, PriceSource.FromSniper)

        calledExactly {
            auction.bid(135)
            sniperListenerSpy.sniperStateChanged(SniperSnapshot(ITEM_ID, 123, 135, BIDDING))
            sniperListenerSpy.sniperStateChanged(SniperSnapshot(ITEM_ID, 135,135, WINNING))
        }
    }

    @Test
    fun bidHigherAndReportsBiddingWhenNewPriceArrives() {
        val price = 1001
        val increment = 25
        val bid = price + increment
        val sniperSnapshot = SniperSnapshot(ITEM_ID, price, bid, BIDDING)

        sniper.currentPrice(price, increment, PriceSource.FromOtherBidder)
        verify {
            auction.bid(bid)
            sniperListenerSpy.sniperStateChanged(sniperSnapshot)
        }
    }
}

fun calledExactly(verifyBlock: MockKVerificationScope.() -> Unit) {
    verify(SEQUENCE, verifyBlock = verifyBlock)
}