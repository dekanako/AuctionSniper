package io.github.dekanako.ui

import io.github.dekanako.FoolishError
import io.github.dekanako.SniperCollector
import io.github.dekanako.SwingThreadSniperListener

import io.github.dekanako.domain.AuctionSniper
import io.github.dekanako.domain.AuctionSniper.SniperSnapshot

import javax.swing.table.AbstractTableModel

class SniperTableModel : AbstractTableModel(), PortfolioListener {

    @Suppress("Not to be GCD")
    private var notToBeGCD: MutableList<AuctionSniper> = mutableListOf()

    private var snapshots: MutableList<SniperSnapshot> = mutableListOf()
    override fun getRowCount(): Int {
        return snapshots.size
    }

    override fun getColumnCount() = MainWindow.Column.values().size
    override fun getColumnName(column: Int): String {
        return MainWindow.Column.at(column).displayName
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return if (snapshots.isEmpty()) ""
        else
            MainWindow.Column.at(columnIndex).valueIn(snapshots[rowIndex])
    }

    fun sniperStatusChanged(sniperSnapshot: SniperSnapshot) {
        val index = snapshots.indexOfFirst { it.isForSameItemAs(sniperSnapshot) }
        throwIfItemWasntAddedBefore(index, sniperSnapshot)
        snapshots[index] = sniperSnapshot
        fireTableDataChanged()
    }

    private fun throwIfItemWasntAddedBefore(index: Int, sniperSnapshot: SniperSnapshot) {
        if (index == -1) throw FoolishError("$sniperSnapshot Should be added before updating it")
    }

    override fun sniperAdded(sniper: AuctionSniper) {
        addSniperSnapshot(sniper.snapshot)
        sniper.addSniperListener(SwingThreadSniperListener(this))
    }
    fun addSniperSnapshot(newSnapshot: SniperSnapshot) {
        snapshots.add(newSnapshot)
        fireTableDataChanged()
    }

    companion object {

        fun textFor(state: SniperSnapshot.SniperStatus): String {
            return STATUS_TEXTS[state.ordinal]
        }

        private val STATUS_TEXTS =
            arrayOf("JOINING", "BIDDING", "WINNING", "LOST", "WON")
    }

}
