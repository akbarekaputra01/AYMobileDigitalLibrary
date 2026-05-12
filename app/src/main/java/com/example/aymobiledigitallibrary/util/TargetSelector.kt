package com.example.aymobiledigitallibrary.util
import com.example.aymobiledigitallibrary.data.LibraryRepository
import com.example.aymobiledigitallibrary.model.LocalRegion
import kotlin.random.Random
object TargetSelector {
    fun globalTargets(participantId: String) = (1..5).map { z -> LibraryRepository.items.filter { it.scrollZoneIndex == z }.random(Random(participantId.hashCode()+z)) }
    fun localTargets(participantId: String) = listOf(LocalRegion.TOP, LocalRegion.MIDDLE, LocalRegion.BOTTOM).flatMapIndexed { i, r -> LibraryRepository.items.filter { it.localRegion == r }.shuffled(Random(participantId.hashCode()+i)).take(2) }
}
