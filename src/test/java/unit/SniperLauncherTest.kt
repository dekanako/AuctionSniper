package unit

import io.github.dekanako.AuctionHouse
import io.github.dekanako.SniperCollector
import io.github.dekanako.SniperLauncher
import io.github.dekanako.domain.Auction
import io.github.dekanako.domain.AuctionSniper
import io.mockk.*
import org.junit.jupiter.api.Test


class SniperLauncherTest {
    val itemId = "dish"

    private val auction = mockk<Auction> {
        every { join() } just Runs
        every { addAuctionEventListener(any()) } just Runs
    }

    private val auctionHouse = mockk<AuctionHouse> {
        every { auctionFor(itemId) } returns auction
    }

    private val collector = mockk<SniperCollector>() {
        every { this@mockk.addSniper(any()) } just Runs
    }

    @Test
    fun addsNewSniperToCollectorAndThenJoinsAuction() {

        val launcher = SniperLauncher(auctionHouse, collector)

        launcher.joinAuction(itemId)

        calledExactly {
            auctionHouse.auctionFor(itemId)
            auction.addAuctionEventListener(match { passed -> passed is AuctionSniper })
            collector.addSniper(any())
            auction.join()
        }
    }
}