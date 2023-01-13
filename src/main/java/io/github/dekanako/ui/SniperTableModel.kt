package io.github.dekanako.ui

import io.github.dekanako.domain.AuctionSniper
import javax.swing.table.AbstractTableModel

class SniperTableModel : AbstractTableModel() {
    private val STARTING_UP = AuctionSniper.SniperSnapshot("", 0, 0, AuctionSniper.SniperSnapshot.SniperStatus.JOINING)
    private var snapshot: AuctionSniper.SniperSnapshot = STARTING_UP
    override fun getRowCount() = 1

    override fun getColumnCount() = MainWindow.Column.values().size
    override fun getColumnName(column: Int): String {
        return MainWindow.Column.at(column).displayName
    }
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return MainWindow.Column.at(columnIndex).valueIn(snapshot)
    }

    fun sniperStatusChanged(sniperSnapshot: AuctionSniper.SniperSnapshot) {
        this.snapshot = sniperSnapshot
        fireTableRowsUpdated(0, 0)
    }

    companion object {
        fun textFor(state: AuctionSniper.SniperSnapshot.SniperStatus): String {
            return STATUS_TEXTS[state.ordinal]
        }

        private val STATUS_TEXTS =
            arrayOf("JOIN", "BIDDING", "WINNING", "LOST", "WON")
    }

}
