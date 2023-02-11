package unit

import com.objogate.wl.swing.probe.ValueMatcherProbe
import e2e.AuctionSniperDriver
import io.github.dekanako.ui.MainWindow
import io.github.dekanako.ui.SniperTableModel
import io.github.dekanako.ui.UserRequestListener
import io.mockk.mockk
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test

class MainWindowTest {

    private val driver = AuctionSniperDriver(1000)

    @Test
    fun makesUserRequestWhenJoinButtonClicked() {
        val buttonProbe = ValueMatcherProbe(Matchers.equalTo("dish"), "join request")

        val listener = UserRequestListener { itemID -> buttonProbe.setReceivedValue(itemID) }

        MainWindow(SniperTableModel()).apply {
            addUserRequestListener(listener)
        }

        driver.startBiddingInFor("dish")

        driver.check(buttonProbe)
    }
}