package com.s63d.domain.api

import org.scoutant.polyline.PolylineDecoder
import org.springframework.data.mongodb.core.geo.GeoJsonLineString
import org.springframework.data.mongodb.core.geo.GeoJsonPoint

data class StepsItem(val duration: Duration,
                     val startLocation: Coordinate,
                     val distance: Distance,
                     val travelMode: String = "",
                     val endLocation: Coordinate,
                     val polyline: Polyline)
{
    companion object {
        val decoder = PolylineDecoder()
    }
    fun toDatabaseModel() : com.s63d.domain.database.StepsItem {
        val points = decoder.decode(polyline.points).map {
            GeoJsonPoint(it.lng, it.lat)
        }
        val line = GeoJsonLineString(points)
        return com.s63d.domain.database.StepsItem(duration, startLocation.toGeoJsonPoint(), distance, travelMode, endLocation.toGeoJsonPoint(), line)
    }
}