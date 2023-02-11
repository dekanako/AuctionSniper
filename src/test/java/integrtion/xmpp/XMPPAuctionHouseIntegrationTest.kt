package integrtion.xmpp

import e2e.ApplicationRunner.Companion.SNIPER_XMPP_ID
import e2e.NewAuctionServer
import e2e.arguments
import io.github.dekanako.*
import io.github.dekanako.domain.AuctionEventListener
import io.github.dekanako.xmpp.XMPPAuctionHouse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class XMPPAuctionHouseIntegrationTest {
    private val countDownLatch = CountDownLatch(1)

    @Test
    fun receivesEventsFromAuctionServerAfterJoining() {
        val server = NewAuctionServer("dish")

        server.startSellingItem()

        val auctionHouse = XMPPAuctionHouse.connect(
            arguments()[ARG_HOST_NAME],
            arguments()[ARG_PORT],
            arguments()[ARG_USER_NAME],
            arguments()[ARG_PASSWORD]
        )

        val auction = auctionHouse.auctionFor("dish")

        auction.addAuctionEventListener(object : AuctionEventListener {
            override fun closed() {
                countDownLatch.countDown()
            }

            override fun currentPrice(price: Int, increment: Int, source: AuctionEventListener.PriceSource) {
            }

        })

        auction.join()
        server.hasReceivedJoinRequestFromSniper(SNIPER_XMPP_ID)
        server.announceClosed()

        Assertions.assertTrue(countDownLatch.await(2, TimeUnit.SECONDS))
    }
}