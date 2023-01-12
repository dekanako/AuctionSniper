package unit

import io.github.dekanako.domain.Auction
import io.github.dekanako.domain.AuctionEventListener.PriceSource
import io.github.dekanako.domain.AuctionSniper
import io.github.dekanako.domain.SniperListener
import io.mockk.MockKVerificationScope
import io.mockk.Ordering.SEQUENCE
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class AuctionSniperTest {
    private val auction = mockk<Auction>(relaxed = true)
    private val sniperListenerSpy = mockk<SniperListener>(relaxed = true)

    private val sniper = AuctionSniper(auction, sniperListenerSpy)

    @Test
    fun reportLostWhenAuctionClosesImmediately() {
        sniper.closed()

        calledExactly {
            sniperListenerSpy.sniperLost()
        }
    }

    @Test
    fun bidWhenPriceReceivedAndFromOtherBidder() {
        sniper.currentPrice(10, 10, PriceSource.FromOtherBidder)

        calledExactly {
            auction.bid(20)
            sniperListenerSpy.sniperBidding()
        }
    }

    @Test
    fun reportLostIfAuctionClosesWhenBidding() {
        sniper.currentPrice(10, 10, PriceSource.FromOtherBidder)
        sniper.closed()
        verify {
            sniperListenerSpy.sniperLost()
        }
    }

    @Test
    fun reportWinIfAuctionClosesWhenWinning() {
        sniper.currentPrice(10, 10, PriceSource.FromOtherBidder)
        sniper.currentPrice(10, 10, PriceSource.FromSniper)

        sniper.closed()

        verify { sniperListenerSpy.sniperWon() }
    }

    @Test
    fun reportWinningWhenCurrentPriceComesFromSniper() {
        sniper.currentPrice(10, 10, PriceSource.FromSniper)

        calledExactly {
            sniperListenerSpy.sniperWinning()
        }
    }
}

fun calledExactly(verifyBlock: MockKVerificationScope.() -> Unit) {
    verify(SEQUENCE, verifyBlock = verifyBlock)
}