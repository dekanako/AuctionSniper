package io.github.dekanako.ui

import io.github.dekanako.domain.AuctionSniper
import io.github.dekanako.ui.SniperTableModel.Companion.textFor
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import java.awt.BorderLayout.NORTH
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.JTextField

const val BID_BUTTON: String = "bid button"
const val ITEM_ID_FIELD: String = "item id fields"
const val MAIN_WINDOW_NAME: String = "main window"
const val SNIPERS_TABLE_NAME = "snipers table"

const val APPLICATION_TITLE = "Auction Sniper"

interface UserRequestListener {
    fun joinAuction(itemID: String)
}

class MainWindow(private val snipers: SniperTableModel, private val listener: UserRequestListener) :
    JFrame(APPLICATION_TITLE) {

    init {
        name = MAIN_WINDOW_NAME
        fillContentPane(makeSnipersTable(), makeControls())
        pack()
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
    }

    private fun makeControls(): JPanel {
        val controls = JPanel(FlowLayout())

        val itemField = JTextField().apply {
            name = ITEM_ID_FIELD
            columns = 100
        }
        val bidButton = JButton("Join Auction").apply {
            name = BID_BUTTON
            addActionListener {
                listener.joinAuction(itemField.text)
            }
        }

        controls.apply {
            add(itemField)
            add(bidButton)
        }

        return controls
    }

    private fun fillContentPane(snipersTable: JTable, controls: JPanel) {
        contentPane.apply {
            layout = BorderLayout()
            add(JScrollPane(snipersTable), CENTER)
            add(controls, NORTH)
        }
    }

    private fun makeSnipersTable() = JTable(snipers).apply {
        name = SNIPERS_TABLE_NAME
    }

    enum class Column(val displayName: String) {

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