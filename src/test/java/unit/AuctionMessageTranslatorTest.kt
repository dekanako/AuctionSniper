package unit

import io.github.dekanako.domain.AuctionEventListener
import io.github.dekanako.domain.AuctionEventListener.PriceSource
import io.github.dekanako.infraRemote.AuctionMessageTranslator
import io.mockk.mockk
import io.mockk.verify
import org.jivesoftware.smack.packet.Message
import org.junit.jupiter.api.Test

class AuctionMessageTranslatorTest {
    private val SNIPER_ID = "sniper"
    private val UNUSED_CHAT = null

    private val auctionEvent = mockk<AuctionEventListener>(relaxed = true)

    private val auctionMessageTranslator = AuctionMessageTranslator(SNIPER_ID, auctionEvent)

    @Test
    fun notifiesAuctionClosedWhenCloseMessageReceived() {
        val message = Message()
        message.body = "SOLVersion: 1.1; Event: CLOSE;"

        auctionMessageTranslator.processMessage(UNUSED_CHAT, message)

        verify {
            auctionEvent.closed()
        }
    }

    @Test
    fun notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
        val message = Message()
        message.body = "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 1922; Increment: 7; Bidder: Bob;"

        auctionMessageTranslator.processMessage(UNUSED_CHAT, message)

        verify {
            auctionEvent.currentPrice(1922, 7, PriceSource.FromOtherBidder)
        }
    }

    @Test
    fun notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
        val message = Message()
        message.body = "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 1922; Increment: 7; Bidder: ${SNIPER_ID};"

        auctionMessageTranslator.processMessage(UNUSED_CHAT, message)

        verify {
            auctionEvent.currentPrice(1922, 7, PriceSource.FromSniper)
        }
    }

    @Test
    fun parsesBlanksCorrectly() {
        val message = Message()
        message.body = "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 1922; Increment: 7; Bidder: other bidder;"

        AuctionMessageTranslator("other bidder", auctionEvent).processMessage(UNUSED_CHAT, message)

        verify {
            auctionEvent.currentPrice(any(), any(), PriceSource.FromSniper)
        }
    }
}
