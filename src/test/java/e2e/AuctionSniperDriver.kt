package e2e

import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.JFrameDriver
import com.objogate.wl.swing.driver.JTableDriver
import com.objogate.wl.swing.gesture.GesturePerformer
import com.objogate.wl.swing.matcher.IterableComponentsMatcher
import com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText
import io.github.dekanako.ui.MAIN_WINDOW_NAME
import org.hamcrest.CoreMatchers.equalTo

class AuctionSniperDriver(timeout: Int) : JFrameDriver(
    GesturePerformer(),
    topLevelFrame(
        named(MAIN_WINDOW_NAME), showingOnScreen()
    ), AWTEventQueueProber(timeout.toLong(), 100)
) {
    fun showsSniperStatus(itemId: String, lastPrice: Int, lastBid: Int, statusText: String) {
        JTableDriver(this).hasRow(
            IterableComponentsMatcher.matching(
                withLabelText(itemId), withLabelText(lastPrice.toString()),
                withLabelText(lastBid.toString()), withLabelText(statusText)
            )
        )
    }

}
