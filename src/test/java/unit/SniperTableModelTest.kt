package unit

import io.github.dekanako.FoolishError
import io.github.dekanako.domain.AuctionSniper
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot.SniperStatus.BIDDING
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot.SniperStatus.JOINING
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
        model.addSniperSnapshot(SniperSnapshot(ITEM_ID, 0, 0, JOINING))
        model.sniperStatusChanged(SniperSnapshot(ITEM_ID, 100, 200, BIDDING))

        assertColumnEquals(ITEM_ID, Column.ITEM_IDENTIFIER)
        assertColumnEquals("100", Column.LAST_PRICE)
        assertColumnEquals("200", Column.LAST_BID)
        assertColumnEquals("BIDDING", Column.SNIPER_STATUS)
    }

    @Test
    fun setsUpColumnHeading() {
        Column.values().forEach {
            Assertions.assertEquals(model.getColumnName(it.ordinal), it.displayName)
        }
    }

    @Test
    fun notifiesListenerWhenAddingASniper() {
        val snapshot = SniperSnapshot.joining("123")
        model.addSniperSnapshot(snapshot)

        Assertions.assertEquals(1, model.rowCount)
        assertRowMatchingSnapshot(0, snapshot)
    }

    @Test
    fun holdsSniperInAdditionOrder() {
        val snapshot = SniperSnapshot.joining("item 0")
        val snapshot2 = SniperSnapshot.joining("item 1")

        model.addSniperSnapshot(snapshot)
        model.addSniperSnapshot(snapshot2)

        Assertions.assertEquals("item 0", cellValue(0, Column.ITEM_IDENTIFIER))
        Assertions.assertEquals("item 1", cellValue(1, Column.ITEM_IDENTIFIER))
    }

    @Test
    fun updateCorrectRowForSniper() {
        val snapshot = SniperSnapshot.joining("item 0")
        val snapshot2 = SniperSnapshot.joining("item 1")
        val expectedSnapshotUpdate = snapshot2.copy(price = 100, bid = 100, state = BIDDING)

        model.addSniperSnapshot(snapshot)
        model.addSniperSnapshot(snapshot2)
        model.sniperStatusChanged(expectedSnapshotUpdate)

        assertRowMatchingSnapshot(1, expectedSnapshotUpdate)
    }

    @Test
    fun throwsDefectWhenNoExistingSniperForAnUpdated() {
        val snapshot = SniperSnapshot.joining("item 0")
        val notExistingSniper = SniperSnapshot.joining("item 432")

        model.addSniperSnapshot(snapshot)

        Assertions.assertThrows(FoolishError::class.java) {
            model.sniperStatusChanged(notExistingSniper)
        }
    }
    private fun cellValue(row: Int, column: Column) = model.getValueAt(row, column.ordinal)

    private fun assertRowMatchingSnapshot(row: Int, snapshot: SniperSnapshot) {
        Column.values().forEach {
            val value = model.getValueAt(row, it.ordinal)
            Assertions.assertEquals(value, it.valueIn(snapshot))
        }
    }

    private fun assertColumnEquals(itemId: Any, column: Column) {
        val row = 0
        val value = model.getValueAt(row, column.ordinal)
        Assertions.assertEquals(itemId, value)
    }

}