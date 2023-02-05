package e2e

import io.github.dekanako.MainRun
import io.github.dekanako.xmpp.BID_FORMAT
import io.github.dekanako.xmpp.JOIN_COMMAND_FORMAT
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit.SECONDS

class NewAuctionServer(private val itemID: String): ItemID {
    private val messageListener = SingleMessageListener()
    private var chat: Chat? = null

    private val connection = XMPPConnection(ConnectionConfiguration(HOST_NAME, 5224))
    override fun itemID(): String {
        return itemID
    }
    fun startSellingItem() {
        connection.connect()
        connection.login(itemID, AUCTION_PASSWORD, AUCTION_RESOURCE)
        connection.chatManager.addChatListener { chat, createdLocally ->
            this@NewAuctionServer.chat = chat
            chat.addMessageListener(messageListener)
        }
    }

    fun hasReceivedJoinRequestFromSniper(sniperId: String) {
        receivesAMessageMatching(sniperId, equalTo(JOIN_COMMAND_FORMAT))
    }
    fun hasReceivedBid(bid: Int, sniperId: String) {
        receivesAMessageMatching(sniperId, equalTo(BID_FORMAT.format(bid)))
    }
    fun announceClosed() {
        chat?.sendMessage("SOLVersion: 1.1; Event: CLOSE;")
    }
    fun reportPrice(price: Int, increment: Int, bidder: String) {
        chat?.sendMessage(
                "SOLVersion: 1.1; Event: PRICE; " +
                        "CurrentPrice: $price; " +
                        "Increment: $increment; " +
                        "Bidder: $bidder;"
        )
    }
    private fun receivesAMessageMatching(sniperId: String, matcher: Matcher<String>) {
        messageListener.receivesAMessageMatching(matcher)
        assertThat(sniperId, equalTo(sniperId))
    }
    fun stop() {
        connection.disconnect()
    }

    private inner class SingleMessageListener : MessageListener {
        private val messageQueue = ArrayBlockingQueue<Message>(10)

        fun receivesAMessageMatching(matcher: Matcher<String>) {
            val message = messageQueue.poll(5, SECONDS)
            assertThat(message, Matchers.hasProperty("body", matcher))
        }

        override fun processMessage(chat: Chat?, message: Message?) {
            messageQueue.add(message)
        }

    }
}