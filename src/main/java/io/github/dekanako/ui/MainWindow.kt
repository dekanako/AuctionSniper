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

const val APPLICATION_TITLE = "Auction Sniper"

class MainWindow(private val snipers: SniperTableModel) : JFrame(APPLICATION_TITLE) {

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

    enum class Column(val displayName: String){

        ITEM_IDENTIFIER("Item") {
            override fun valueIn(snapshot: AuctionSniper.SniperSnapshot) = snapshot.itemId
        },
        LAST_PRICE("Last Price") {
            override fun valueIn(snapshot: AuctionSniper.SniperSnapshot) = snapshot.price.toString()
        },
        LAST_BID("Last Bid") {
            override fun valueIn(snapshot: AuctionSniper.SniperSnapshot) = snapshot.bid.toString()
        },
        SNIPER_STATUS("State") {
            override fun valueIn(snapshot: AuctionSniper.SniperSnapshot): String = textFor(snapshot.state)
        };

        abstract fun valueIn(snapshot: AuctionSniper.SniperSnapshot): String

        companion object {
            fun at(offset: Int): Column {
                return values()[offset]
            }
        }
    }
}