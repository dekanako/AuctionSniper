package io.github.dekanako.infraRemote

import io.github.dekanako.domain.AuctionEventListener
import io.github.dekanako.domain.AuctionEventListener.PriceSource
import io.github.dekanako.domain.AuctionEventListener.PriceSource.FromOtherBidder
import io.github.dekanako.domain.AuctionEventListener.PriceSource.FromSniper
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.packet.Message

private const val EVENT = "Event"

private const val CURRENT_PRICE = "CurrentPrice"
private const val INCREMENT = "Increment"

class AuctionMessageTranslator(private val sniperId: String, private val eventListener: AuctionEventListener) : MessageListener {

    override fun processMessage(chat: Chat?, message: Message?) {
        val event = AuctionEvent(message)
        when (event.type()) {
            "CLOSE" -> eventListener.closed()
            "PRICE" -> eventListener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId))
        }
    }

}
private class AuctionEvent(message: Message?) {
    private val fields = unpackEventFrom(message);
    fun currentPrice() = getInt(CURRENT_PRICE)
    fun increment() = getInt(INCREMENT)
    fun type() = get(EVENT)
    fun bidder(): String = get("Bidder").orEmpty()
    fun isFrom(sniperId: String): PriceSource {
        return if (bidder() == sniperId) FromSniper else FromOtherBidder
    }

    private fun get(key: String) = fields[key]
    private fun getInt(key: String) = Integer.parseInt(fields[key])

    companion object {
        private fun unpackEventFrom(message: Message?): HashMap<String, String> {
            val hashMap = hashMapOf<String, String>()
            val field = extractFields(message)
            field.forEach {
                val list = it.split(":")
                hashMap[list[0].trim()] = list[1].trim()
            }
            return hashMap
        }

        private fun extractFields(message: Message?): List<String> {
            val body = message?.body

            return body?.split(';').orEmpty().toMutableList().apply { removeLast() }
        }
    }
}
