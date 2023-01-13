package unit

import io.github.dekanako.domain.AuctionSniper.SniperSnapshot
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot.SniperStatus.BIDDING
import io.github.dekanako.ui.MainWindow.Column
import io.github.dekanako.ui.SniperTableModel
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.event.TableModelListener

class SniperTableModelTest {
    private val tableModelListener = mockk<TableModelListener>(relaxed = true)
    private val model = SniperTableModel()
    @BeforeEach
    fun setup() {
        model.addTableModelListener(tableModelListener)
    }

    @Test
    fun hasEnoughColumns() {
        Assertions.assertEquals(Column.values().size, model.columnCount)
    }

    @Test
    fun setSniperValuesInColumns() {
        model.sniperStatusChanged(SniperSnapshot(ITEM_ID, 100, 200, BIDDING))

        assertColumnEquals(ITEM_ID, Column.ITEM_IDENTIFIER)
        assertColumnEquals("100", Column.LAST_PRICE)
        assertColumnEquals("200", Column.LAST_BID)
        assertColumnEquals("BIDDING", Column.SNIPER_STATUS)
    }

    private fun assertColumnEquals(itemId: Any, column: Column) {
        val row = 0
        val value = model.getValueAt(row, column.ordinal)
        Assertions.assertEquals(itemId, value)
    }

}