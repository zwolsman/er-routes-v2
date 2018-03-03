package com.s63d.domain.api

data class LegsItem(val duration: Duration,
                    val startLocation: Coordinate,
                    val distance: Distance,
                    val startAddress: String = "",
                    val endLocation: Coordinate,
                    val endAddress: String = "",
                    val steps: List<StepsItem>) {
    fun toDatabaseModel() : com.s63d.domain.database.LegsItem {
        return com.s63d.domain.database.LegsItem(duration, startLocation.toGeoJsonPoint(), distance, startAddress, endLocation.toGeoJsonPoint(), endAddress, steps.map(StepsItem::toDatabaseModel))
    }
}