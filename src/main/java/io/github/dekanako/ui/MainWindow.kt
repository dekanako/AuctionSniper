package io.github.dekanako.ui

import io.github.dekanako.domain.AuctionSniper
import io.github.dekanako.ui.SniperTableModel.Companion.textFor
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTable

const val MAIN_WINDOW_NAME: String = "main window"
const val SNIPERS_TABLE_NAME = "snipers table"

class MainWindow(private val snipers: SniperTableModel) : JFrame("Auction Sniper") {

    init {
        name = MAIN_WINDOW_NAME
        fillContentPane(makeSnipersTable())
        pack()
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
    }

    private fun fillContentPane(snipersTable: JTable) {
        contentPane.apply {
            layout = BorderLayout()
            add(JScrollPane(snipersTable), CENTER)
        }
    }

    private fun makeSnipersTable() = JTable(snipers).apply {
        name = SNIPERS_TABLE_NAME
    }

    enum class Column {
        ITEM_IDENTIFIER {
            override fun valueIn(snapshot: AuctionSniper.SniperSnapshot) = snapshot.itemId
        },
        LAST_PRICE {
            override fun valueIn(snapshot: AuctionSniper.SniperSnapshot) = snapshot.price.toString()
        },
        LAST_BID {
            override fun valueIn(snapshot: AuctionSniper.SniperSnapshot) = snapshot.bid.toString()
        },
        SNIPER_STATUS {
            override fun valueIn(snapshot: AuctionSniper.SniperSnapshot): String {
                val textFor = textFor(snapshot.state)
                return textFor
            }
        };

        abstract fun valueIn(snapshot: AuctionSniper.SniperSnapshot): String

        companion object {
            fun at(offset: Int): Column {
                return values()[offset]
            }
        }
    }
}