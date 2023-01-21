package e2e

import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.JButtonDriver
import com.objogate.wl.swing.driver.JFrameDriver
import com.objogate.wl.swing.driver.JTableDriver
import com.objogate.wl.swing.driver.JTableHeaderDriver
import com.objogate.wl.swing.driver.JTextFieldDriver
import com.objogate.wl.swing.gesture.GesturePerformer
import com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching
import com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText
import io.github.dekanako.ui.*
import javax.swing.JButton
import javax.swing.JTextField
import javax.swing.table.JTableHeader

class AuctionSniperDriver(timeout: Int) : JFrameDriver(
    GesturePerformer(),
    topLevelFrame(
        named(MAIN_WINDOW_NAME), showingOnScreen()
    ), AWTEventQueueProber(timeout.toLong(), 100)
) {
    fun showsSniperStatus(itemId: String, lastPrice: Int, lastBid: Int, statusText: String) {
        JTableDriver(this).hasRow(
            matching(
                withLabelText(itemId), withLabelText(lastPrice.toString()),
                withLabelText(lastBid.toString()), withLabelText(statusText)
            )
        )
    }

    fun hasColumnTitles() {
        val headers = JTableHeaderDriver(this, JTableHeader::class.java)
        headers.hasHeaders(
            matching(
                withLabelText("Item"), withLabelText("Last Price"), withLabelText("Last Bid"), withLabelText("State")
            )
        )
    }

    fun startBiddingInFor(itemId: String) {
        when (itemId) {
            "dish" -> {
                itemIdField().clearText()
                itemIdField().typeText(itemId[0].plus(itemId))
            }

            "tv" -> {
                itemIdField().clearText()
                itemIdField().typeText(itemId)
            }
        }
//        itemIdField().replaceAllText((itemId))
        bidButton().click()
    }

    private fun itemIdField(): JTextFieldDriver {
        return JTextFieldDriver(this, JTextField::class.java, named(ITEM_ID_FIELD)).apply {
            focusWithMouse()
        }
    }

    private fun bidButton() = JButtonDriver(this, JButton::class.java, named(BID_BUTTON))

}
