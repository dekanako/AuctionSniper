package io.github.dekanako.infraRemote

import io.github.dekanako.MainRun
import io.github.dekanako.domain.Auction
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.XMPPException

class XMPPAuction(private val chat: Chat) : Auction {
    override fun bid(amount: Int) {
        try {
            chat.sendMessage(String.format(MainRun.BID_FORMAT, amount))
        } catch (e: XMPPException) {
            e.printStackTrace()
        }
    }

    override fun join() {
        chat.sendMessage(MainRun.JOIN_COMMAND_FORMAT)
    }
}