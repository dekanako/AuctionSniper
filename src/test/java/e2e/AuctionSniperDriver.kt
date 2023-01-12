package e2e

import com.objogate.wl.swing.AWTEventQueueProber
import com.objogate.wl.swing.driver.JFrameDriver
import com.objogate.wl.swing.driver.JLabelDriver
import com.objogate.wl.swing.gesture.GesturePerformer
import io.github.dekanako.UI.MAIN_WINDOW_NAME
import io.github.dekanako.UI.SNIPER_STATUS_NAME
import org.hamcrest.CoreMatchers.equalTo

class AuctionSniperDriver(timeout: Int) : JFrameDriver(
    GesturePerformer(),
    topLevelFrame(
        named(MAIN_WINDOW_NAME), showingOnScreen()
    ), AWTEventQueueProber(timeout.toLong(), 100)
) {
    fun showsSniperStatus(status: String) {
        JLabelDriver(this, named(SNIPER_STATUS_NAME)).hasText(equalTo(status))
    }

}
