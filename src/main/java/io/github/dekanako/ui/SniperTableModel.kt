package io.github.dekanako.ui

import io.github.dekanako.FoolishError
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot
import javax.swing.table.AbstractTableModel

class SniperTableModel : AbstractTableModel() {
    private var snapshot: MutableList<SniperSnapshot> = mutableListOf()
    override fun getRowCount(): Int {
        return snapshot.size
    }

    override fun getColumnCount() = MainWindow.Column.values().size
    override fun getColumnName(column: Int): String {
        return MainWindow.Column.at(column).displayName
    }
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return if (snapshot.isEmpty()) ""
        else
            MainWindow.Column.at(columnIndex).valueIn(snapshot[rowIndex])
    }

    fun sniperStatusChanged(sniperSnapshot: SniperSnapshot) {
        val index = snapshot.indexOfFirst { it.isForSameItemAs(sniperSnapshot) }
        throwIfItemWasntAddedBefore(index, sniperSnapshot)
        snapshot[index] = sniperSnapshot
        fireTableRowsUpdated(index, index + 1)
    }

    private fun throwIfItemWasntAddedBefore(index: Int, sniperSnapshot: SniperSnapshot) {
        if (index == -1) throw FoolishError("$sniperSnapshot Should be added before updating it")
    }

    fun addSniper(newSnapshot: SniperSnapshot) {
        snapshot.add(newSnapshot)
        fireTableRowsInserted(snapshot.lastIndex - 1, snapshot.lastIndex)
    }

    companion object {
        fun textFor(state: SniperSnapshot.SniperStatus): String {
            return STATUS_TEXTS[state.ordinal]
        }

        private val STATUS_TEXTS =
            arrayOf("JOINING", "BIDDING", "WINNING", "LOST", "WON")
    }

}
