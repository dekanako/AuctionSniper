package e2e

import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.JFrameDriver
import com.objogate.wl.swing.driver.JTableDriver
import com.objogate.wl.swing.driver.JTableHeaderDriver
import com.objogate.wl.swing.gesture.GesturePerformer
import com.objogate.wl.swing.matcher.IterableComponentsMatcher
import com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching
import com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText
import io.github.dekanako.ui.MAIN_WINDOW_NAME
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

}
