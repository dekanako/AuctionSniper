package unit

import io.github.dekanako.domain.AuctionSniper.SniperSnapshot.Companion.joining
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SniperSnapshotTest {
    @Test
    fun trueWhenSnapshotHaveSameId() {
        val snapshot = joining("item 1")

        Assertions.assertTrue(snapshot.isForSameItemAs(snapshot))
    }
    @Test
    fun falseWhenSnapshotHaveDifferentIds() {
        val snapshot = joining("item 1")
        val anotherSnapshot = joining("item 0")

        Assertions.assertFalse(snapshot.isForSameItemAs(anotherSnapshot))
    }
}