package io.github.dekanako

import io.github.dekanako.domain.AuctionSniper
import io.github.dekanako.domain.SniperListener

import io.github.dekanako.ui.SniperTableModel

import javax.swing.SwingUtilities

class SwingThreadSniperListener(private val snipers: SniperTableModel) : SniperListener {
    override fun sniperStateChanged(sniperSnapshot: AuctionSniper.SniperSnapshot) {
        SwingUtilities.invokeLater {
            snipers.sniperStatusChanged(sniperSnapshot)
        }
    }
}